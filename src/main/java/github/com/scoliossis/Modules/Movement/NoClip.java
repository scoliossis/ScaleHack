package github.com.scoliossis.Modules.Movement;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoClip {
    public static boolean noclipOverride;

    public static boolean shouldNoClip() {
        return Configs.noClip || noclipOverride;
    }

    @SubscribeEvent
    public void onPostMotion(MotionEvent.Post event) {
        if (!Configs.noClip) return;

        if (Configs.noClipOnlyCrouchDown && !C.mc.gameSettings.keyBindSneak.isKeyDown())
            C.p().motionY = 0;
    }
}
