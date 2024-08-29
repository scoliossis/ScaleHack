package github.com.scoliossis.Modules.Movement;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.MoveFlyingEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.MathUtil;
import github.com.scoliossis.Util.MovementUtil;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed {

    Vec3 spoofPos = null;

    float prevYaw = 0;


    @SubscribeEvent
    public void onPreMotion(MotionEvent.Pre event) {
        if (C.p().onGround) MovementUtil.airTicks = 0;
        else MovementUtil.airTicks++;


        if (Configs.speed) {
            switch (Configs.speedMode) {
                case 0:
                    MovementUtil.setMotion(Configs.speedSpeed);
                    break;

                case 1:
                    if (C.p().onGround && C.mc.gameSettings.keyBindJump.isKeyDown()) {
                        C.p().jump();
                        MovementUtil.setMotion(0.25f);
                    }
                    break;
            }
        }

        prevYaw = MovementUtil.getMovementYaw();
    }

    @SubscribeEvent
    public void onMoveFlying(MoveFlyingEvent event) {
        if (Configs.speed) {
            switch (Configs.speedMode) {
                case 2:
                    event.friction = Configs.speedFriction;
                    break;
            }
        }
    }
}
