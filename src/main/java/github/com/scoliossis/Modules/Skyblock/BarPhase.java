package github.com.scoliossis.Modules.Skyblock;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Modules.Visuals.BlockESP;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.MovementUtil;
import github.com.scoliossis.Util.TeleportUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BarPhase {

    int collisionTicks = 0;

    @SubscribeEvent
    public void onMotionPost(MotionEvent.Post event) {
        if (!Configs.barPhase) return;

        if (C.p().isCollidedHorizontally && insidePhaseableBlock()) {
            EnumFacing facing = EnumFacing.fromAngle(MovementUtil.getMovementYaw());

            if (isInfrontOfAir(facing)) {
                collisionTicks++;

                if (collisionTicks > 2 && C.p().onGround) {
                    TeleportUtil.hClip(0.6);

                    collisionTicks = 0;
                }

                return;
            }
        }

        collisionTicks = 0;
    }

    private boolean insidePhaseableBlock() {
        BlockPos pos = new BlockPos(C.p().posX, C.p().posY, C.p().posZ);

        return  BlockESP.blockAtPos(pos) == Blocks.iron_bars ||
                BlockESP.blockAtPos(pos) == Blocks.glass_pane ||
                BlockESP.blockAtPos(pos) == Blocks.stained_glass ||

                BlockESP.blockAtPos(pos.up()) == Blocks.iron_bars ||
                BlockESP.blockAtPos(pos.up()) == Blocks.glass_pane ||
                BlockESP.blockAtPos(pos.up()) == Blocks.stained_glass;
    }

    private boolean isInfrontOfAir(EnumFacing facing) {
        Vec3 loc = new Vec3(C.p().posX, C.p().posY, C.p().posZ);
        Vec3 offsVec = new Vec3(0.0, 1.0,0.0).crossProduct(new Vec3(facing.getDirectionVec()));
        offsVec = new Vec3(offsVec.xCoord * 0.3f, offsVec.yCoord * 0.3f, offsVec.zCoord * 0.3f);


        return C.w().getBlockState(new BlockPos(loc.add(offsVec)).offset(facing)).getBlock() == Blocks.air &&
                C.w().getBlockState(new BlockPos(loc.subtract(offsVec)).offset(facing)).getBlock() == Blocks.air &&
                C.w().getBlockState(new BlockPos(loc.add(offsVec)).offset(facing).up()).getBlock() == Blocks.air &&
                C.w().getBlockState(new BlockPos(loc.subtract(offsVec)).offset(facing).up()).getBlock() == Blocks.air;
    }
}
