package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Events.BlockChangeEvent;
import github.com.scoliossis.Events.ChunkLoadEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockESP {
    public static Block blockAtPos(BlockPos pos) {
        return C.w().getBlockState(pos).getBlock();
    }

    static Block[] blocksArray = new Block[] {Blocks.bed, Blocks.lever, Blocks.chest, Blocks.trapped_chest, Blocks.skull};
    static ArrayList<Block> blocksToScanFor = new ArrayList<>();
    static {
        blocksToScanFor.addAll(Arrays.asList(blocksArray));
    }

    static float[][] blockSizes = new float[][] {{1, 0.6f}, {0.5f, 0.5f}, {0.9f, 0.9f}, {0.9f, 0.9f}, {0.5f, 0.5f}};

    static ArrayList<ChunkLoadEvent> chunksToScan = new ArrayList<>();
    static ArrayList<ChunkEvent.Unload> chunksToRemove = new ArrayList<>();
    static ArrayList<BlockChangeEvent> changedBlocks = new ArrayList<>();

    static ArrayList<BlockPos> blocks = new ArrayList<>();

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        blocks.clear();
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkLoadEvent event) {
        chunksToScan.add(event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Configs.blockESP) scan();
        //ForkJoinPool.commonPool().execute(BlockESP::scan);
    }

    static boolean scanning = false;

    private static void scan() {
        ChunkLoadEvent[] chunksToAdd = chunksToScan.toArray(new ChunkLoadEvent[0]);
        ChunkEvent.Unload[] chunksToDelete = chunksToRemove.toArray(new ChunkEvent.Unload[0]);
        BlockChangeEvent[] blockChanges = changedBlocks.toArray((new BlockChangeEvent[0]));

        scanChunks(chunksToAdd);
        removeChunks(chunksToDelete);
        changeBlocks(blockChanges);
        scanning = false;
    }

    private static void scanChunks(ChunkLoadEvent[] chunks) {
        for (ChunkLoadEvent chunk : chunks) {
            chunksToScan.remove(chunk);

            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < 256; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos blockPos = new BlockPos(chunk.getChunk().xPosition * 16 + x, y, chunk.getChunk().zPosition * 16 + z);

                        if (blocksToScanFor.contains(blockAtPos(blockPos))) blocks.add(blockPos);
                    }
                }
            }
        }
    }

    private static void removeChunks(ChunkEvent.Unload[] chunksToDelete) {
        for (ChunkEvent.Unload chunk : chunksToDelete) {
            chunksToRemove.remove(chunk);

            ArrayList<BlockPos> toRemove = new ArrayList<>();

            for (BlockPos bed : blocks) {
                if (bed != null && chunk != null) {
                    if (bed.getX() >= chunk.getChunk().xPosition * 16 && bed.getX() < chunk.getChunk().xPosition * 16 + 16) {
                        if (bed.getZ() >= chunk.getChunk().zPosition * 16 && bed.getZ() < chunk.getChunk().zPosition * 16 + 16) {
                            toRemove.add(bed);
                        }
                    }
                }
            }

            blocks.removeAll(toRemove);
        }
    }

    private static void changeBlocks(BlockChangeEvent[] blockChanges) {
        for (BlockChangeEvent block : blockChanges) {
            changedBlocks.remove(block);

            // not really needed because i alr check if it is real or not when rendering
            //if (blocksToScanFor.contains(block.old.getBlock())) blocks.remove(block.pos);
            if (block != null && blocksToScanFor.contains(block.update.getBlock()) && !blocks.contains(block.pos))
                blocks.add(block.pos);
        }
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        chunksToRemove.add(event);
    }

    // hopefully not too fps taxing...
    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        changedBlocks.add(event);
    }



    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Configs.blockESP) return;

        BlockPos[] blocksToRender = blocks.toArray(new BlockPos[0]);

        for (BlockPos pos : blocksToRender) {
            Block block = blockAtPos(pos);
            if (!blocksToScanFor.contains(block)) {
                blocks.remove(pos);
                continue;
            }

            float[] size = blockSizes[blocksToScanFor.indexOf(block)];

            Color[] colors = RenderUtil.getColorsFade(pos.getY() * 100, 100 * size[1], RenderUtil.rainbowColors, 3f);

            RenderUtil.renderBox3Dfade(pos.getX() + (0.5f - size[0] / 2f), pos.getY(), pos.getZ() + (0.5f - size[0] / 2f), size[0], size[1], event.partialTicks, RenderUtil.setColorOpacity(colors[0], 100), RenderUtil.setColorOpacity(colors[1], 100));
        }
    }
}