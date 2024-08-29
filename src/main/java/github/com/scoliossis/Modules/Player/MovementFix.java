package github.com.scoliossis.Modules.Player;

import github.com.scoliossis.Events.JumpEvent;
import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.MoveFlyingEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.MovementUtil;
import github.com.scoliossis.Util.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MovementFix {
    public static float playerYaw = 0;
    public static float playerPitch = 0;
    public static boolean fixMovement = false;

    public static boolean postedEvent = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRotationEvent(RotationEvent event) {
        playerYaw = event.yaw;
        playerPitch = event.pitch;

        postedEvent = true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onMotionEvent(MotionEvent.Post event) {
        postedEvent = false;
    }

    public static boolean shouldFixMovement() {
        return Configs.moveFix;
    }

    public static boolean shouldFixLookPos() {
        return Configs.moveFix || Configs.freelook;
    }


    public static MovingObjectPosition fixedLookingPos() {
        //return entity.rayTrace(d0, p_getMouseOver_1_)
        return RotationUtil.rayTrace(C.mc.playerController.getBlockReachDistance(), playerYaw, playerPitch);
    }

    public static boolean overrideSprint = false;

    @SubscribeEvent
    public void onMoveFlying(MoveFlyingEvent event) {
        if (shouldFixMovement()) {
            event.yaw = playerYaw;

            if (Configs.moveFixMode == 1) {
                if ((C.p().onGround && !C.mc.gameSettings.keyBindJump.isKeyDown()) || !C.p().isSprinting()) {
                    fixStrafe(event);

                    if (C.p().isSprinting() && event.forward <= 0) {
                        event.friction /= 1.3f;
                        C.p().setSprinting(false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onJumpEvent(JumpEvent event) {
        if (shouldFixMovement()) event.yaw = playerYaw;
    }

    // pasted from idk where, but it was in mushroom so its mine now
    private void fixStrafe(MoveFlyingEvent event) {
        int dif = (int) ((MathHelper.wrapAngleTo180_float(C.p().rotationYaw - event.yaw - 158.5f) + 180.0f) / 45f);

        float strafe = event.strafe;
        float forward = event.forward;

        float calcForward = 0;
        float calcStrafe = 0;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }

            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }

            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }

            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }

            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }

            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }

            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }

            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }

        if (calcForward > 1.0F || (calcForward < 0.9F && calcForward > 0.3F) || calcForward < -1.0F || (calcForward > -0.9F && calcForward < -0.3F))
            calcForward *= 0.5F;

        if (calcStrafe > 1.0F || (calcStrafe < 0.9F && calcStrafe > 0.3F) || calcStrafe < -1.0F || (calcStrafe > -0.9F && calcStrafe < -0.3F))
            calcStrafe *= 0.5F;

        event.forward = calcForward;
        event.strafe = calcStrafe;
    }
}
