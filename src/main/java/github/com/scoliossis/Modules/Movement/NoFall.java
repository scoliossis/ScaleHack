package github.com.scoliossis.Modules.Movement;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Events.PacketEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.PacketUtil;
import github.com.scoliossis.Util.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall {


    float lastFallDist = 0;

    boolean timered = false;

    @SubscribeEvent
    public void onMotion(MotionEvent.Pre event) {
        if (!Configs.noFall) return;


        switch (Configs.noFallMode) {
            case 0:
                event.onGround = false;
                break;
            case 1:
                event.onGround = true;
                break;

            case 2:
                if (C.p().fallDistance-C.p().motionY>3) {
                    C.p().fallDistance = 0;
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(C.p().posX, C.p().getEntityBoundingBox().minY, C.p().posZ, true));
                }
                break;

            case 3:
                switch (Configs.hypixelNoFallMode) {
                    case 0:
                        if (C.p().fallDistance-C.p().motionY>=3) {
                            C.p().fallDistance = 0;
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(C.p().posX, C.p().getEntityBoundingBox().minY, C.p().posZ, true));
                            TimerUtil.setTimer(0.5f);
                            timered = true;
                        }
                        else if (timered) {
                            TimerUtil.setTimer(1);
                            timered = false;
                        }
                        break;
                    case 1:
                        if (C.p().fallDistance-C.p().motionY>=3) {
                            C.p().fallDistance = 0;
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(C.p().posX, C.p().getEntityBoundingBox().minY, C.p().posZ, true));
                            TimerUtil.setTimer(0.5f);
                            timered = true;
                        }
                        else if (C.p().onGround && timered) {
                            TimerUtil.setTimer(1);
                            timered = false;
                        }
                        break;
                    case 2:
                        if (C.p().onGround && lastFallDist > 3) {
                            event.onGround = false;
                            C.p().jump();
                        }
                        break;
                }

                break;

            case 4:
                // shoutout to swig + batman for nofall method!
                if (C.p().fallDistance-C.p().motionY >= 3) NoClip.noclipOverride = true;
                break;
        }

        lastFallDist = C.p().fallDistance;
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(PacketEvent.Receive.Pre event) {
        // on flag
        if (event.packet instanceof S08PacketPlayerPosLook) {
            if (Configs.noFallMode == 4 && NoClip.noclipOverride) {
                NoClip.noclipOverride = false;
                C.p().fallDistance = 0;
            }
        }
    }
}
