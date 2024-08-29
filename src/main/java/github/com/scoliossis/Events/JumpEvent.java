package github.com.scoliossis.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class JumpEvent extends Event {
    public float yaw;
    public double height;

    public JumpEvent(float yaw, double height) {
        this.yaw = yaw;
        this.height = height;
    }
}