package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerSideRotations {

    public static float yaw, pitch;

    public static float overrideYaw, overridePitch = 1000.9f;

    @SubscribeEvent
    public void onPreMotion(MotionEvent.Post event) {
        if (!Configs.showRots) return;

        yaw = event.yaw;
        pitch = event.pitch;

        if (overrideYaw != 1000.9f) {
            yaw = overrideYaw;
            overrideYaw = 1000.9f;
        }

        if (overridePitch != 1000.9f) {
            pitch = overridePitch;
            overridePitch = 1000.9f;
        }
    }
}
