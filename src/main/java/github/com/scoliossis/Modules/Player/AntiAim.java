package github.com.scoliossis.Modules.Player;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.RotationEvent;
import github.com.scoliossis.Modules.Visuals.ServerSideRotations;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiAim {


    @SubscribeEvent
    public void onPreMotion(RotationEvent event) {
        if (!Configs.antiAim) return;

        float yaw = (float) (Math.random() * 360);
        float pitch = (float) (Math.random() * 180) - 90;

        if (Configs.serverSideAntiAim) {
            event.yaw = (int)yaw;
            event.pitch = (int)pitch;
        }
        else {
            ServerSideRotations.overrideYaw = yaw;
            ServerSideRotations.overridePitch = pitch;
        }
    }
}
