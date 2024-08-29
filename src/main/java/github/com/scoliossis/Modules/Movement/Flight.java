package github.com.scoliossis.Modules.Movement;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.MovementUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Flight {

    @SubscribeEvent
    public void onPreMotion(MotionEvent.Pre event) {
        if (!Configs.flight) return;

        MovementUtil.setMotion(Configs.flightHorizontalSpeed);

        C.p().motionY = 0;
        if (C.mc.gameSettings.keyBindJump.isKeyDown()) C.p().motionY = Configs.flightVerticalSpeed;
        if (C.mc.gameSettings.keyBindSneak.isKeyDown()) C.p().motionY = -Configs.flightVerticalSpeed;
    }
}
