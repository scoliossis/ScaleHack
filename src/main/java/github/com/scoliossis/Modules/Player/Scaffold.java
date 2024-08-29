package github.com.scoliossis.Modules.Player;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.*;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Scaffold {

    public static boolean isOverAir() {
        double x = C.p().posX;
        double y = C.p().posY - 1D;
        double z = C.p().posZ;
        BlockPos p = new BlockPos(Math.floor(x), Math.floor(y), Math.floor(z));
        return C.w().getBlockState(p).getBlock() instanceof BlockAir;
    }


    // not on rotation event, because it doesnt need move fix
    @SubscribeEvent
    public void onRotationEvent(RotationEvent event) {
        if (!Configs.scaffold) return;

        int heldSlot = getBlock();
        if (heldSlot != -1) {
            C.p().inventory.currentItem = heldSlot;

            float yaw = MovementUtil.getMovementYaw() - 180;
            int minPitch = 70;
            int maxPitch = C.p().onGround ? 81 : 90;

            //event.yaw = yaw;
            //event.pitch = maxPitch;

            //if (C.p().onGround) MovementUtil.setMotion(0.08);

            /*
            if (C.mc.gameSettings.keyBindJump.isKeyDown()) {
                switch (MovementUtil.airTicks % 3) {
                    case 0:
                        C.p().motionY = 0.42f;
                        break;
                    case 1:
                        C.p().motionY = 0.33f;
                        break;
                    case 2:
                        C.p().motionY = 1 - C.p().posY % 1;
                        break;
                }
            }
             */

            for (int i = minPitch; i < maxPitch; i++) {
                MovingObjectPosition blockHit = RotationUtil.rayTrace(i, yaw);
                if (isValidBlockPlace(i, yaw) && isOverAir()) {
                    event.yaw = (float) (yaw+Math.random());
                    event.pitch = (float) (i+Math.random());

                    Vec3 hitVec = blockHit.hitVec;
                    BlockPos hitPos = blockHit.getBlockPos();
                    float f = (float)(hitVec.xCoord - hitPos.getX());
                    float f2 = (float)(hitVec.yCoord - hitPos.getY());
                    float f3 = (float)(hitVec.zCoord - hitPos.getZ());
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(blockHit.getBlockPos(), blockHit.sideHit.getIndex(), C.p().getHeldItem(), f, f2, f3));
                    C.p().swingItem();
                    C.p().getHeldItem().onItemUse(C.p(), C.w(), hitPos, blockHit.sideHit, f, f2, f3);

                    break;
                    /*
                    if (C.mc.playerController.onPlayerRightClick(C.p(), C.w(), C.p().inventory.getStackInSlot(heldSlot), blockHit.getBlockPos(), blockHit.sideHit, blockHit.hitVec)) {
                        C.p().swingItem();

                        event.yaw = yaw;
                        event.pitch = maxPitch;

                        if (C.p().inventory.getStackInSlot(heldSlot) != null && C.p().inventory.getStackInSlot(heldSlot).stackSize <= 0) {
                            C.p().inventory.removeStackFromSlot(heldSlot);
                        }
                        break;
                    }
                     */
                }
            }
        }
    }


    private boolean isValidBlockPlace(float pitch, float yaw) {
        MovingObjectPosition blockHit = RotationUtil.rayTrace(pitch, yaw);
        if (blockHit != null && blockHit.getBlockPos() != null) {
            //blockHit.getBlockPos().offset(blockHit.sideHit).getY() < C.p().getPosition().getY()-C.p().motionY-1
            return blockHit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                    blockHit.getBlockPos().offset(blockHit.sideHit).getY() < C.p().getPosition().getY();
        }
        return false;
    }


    private int getBlock() {
        if (C.p().getHeldItem() != null && C.p().getHeldItem().getItem() instanceof ItemBlock) return C.p().inventory.currentItem;
        else {
            int current = -1;

            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = C.p().inventory.getStackInSlot(i);
                if (itemStack != null) {
                    Item item = itemStack.getItem();
                    if (item instanceof ItemBlock && (current == -1 || itemStack.stackSize > C.p().inventory.getStackInSlot(current).stackSize)) {
                        current = i;
                    }
                }
            }

            return current;
        }
    }
}
