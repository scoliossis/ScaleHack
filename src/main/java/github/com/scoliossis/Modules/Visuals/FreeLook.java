package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Events.*;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.RotationUtil;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreeLook {
    // no onenable / ondisable yet, prob need .-.
    boolean enabled;

    float playerYaw = 0;
    float playerPitch = 0;
    int camera = 0;

    // highest priority to make sure it doesnt override other rotations
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPreMotion(RotationEvent event) {
        if (Configs.freelook) {
            if (!enabled) {
                playerYaw = C.p().rotationYaw;
                playerPitch = C.p().rotationPitch;

                camera = C.mc.gameSettings.thirdPersonView;
                C.mc.gameSettings.thirdPersonView = 1;
            }


            event.yaw = playerYaw;
            event.pitch = playerPitch;
        }

        enabled = Configs.freelook;
    }



    // fix movement n shit

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Configs.freelook && enabled) {
            C.p().prevRotationYaw = playerYaw;
            C.p().prevRotationPitch = playerPitch;
            C.p().rotationYaw = playerYaw;
            C.p().rotationPitch = playerPitch;

            enabled = false;

            if (camera != -1) C.mc.gameSettings.thirdPersonView = camera;
            camera = -1;
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMoveFlying(MoveFlyingEvent event) {
        if (Configs.freelook && camera != -1) event.yaw = playerYaw;
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onJumpEvent(JumpEvent event) {
        if (Configs.freelook && camera != -1) event.yaw = playerYaw;
    }
}
