package github.com.scoliossis.Util;

import github.com.scoliossis.mixins.MinecraftInterface;
import net.minecraft.util.Timer;

public class TimerUtil {
    public static void setTimer(float speed) {
        getTimer().timerSpeed = speed;
    }

    public static Timer getTimer() {
        return ((MinecraftInterface)C.mc).getTimer();
    }
}
