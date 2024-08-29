package github.com.scoliossis.Modules.Skyblock;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Modules.Visuals.BlockESP;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class GhostBlocks {

    private final Block[] whitelist = new Block[] {Blocks.skull, Blocks.chest, Blocks.trapped_chest, Blocks.air, Blocks.lever, Blocks.stone_button, Blocks.wooden_button};

    int ticks = 0;

    @SubscribeEvent
    public void onPreMotion(MotionEvent.Pre event) {
        if (!Configs.gKey) return;

        ticks++;

        if (ticks > Configs.gKeyDelay) {
            BlockPos blockToGhost = C.p().rayTrace(Configs.gKeyReach, 1f).getBlockPos();

            if (blockToGhost != null) {
                if (!Configs.gKeyWhitelist || Arrays.stream(whitelist).anyMatch(e -> e == BlockESP.blockAtPos(blockToGhost))) {
                    C.w().setBlockToAir(blockToGhost);
                    ticks = 0;
                }
            }
        }
    }
}
