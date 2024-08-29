package github.com.scoliossis.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveFlyingEvent extends Event {
    public float strafe;
    public float forward;
    public float friction;
    public float yaw;

    public MoveFlyingEvent(float strafe, float forward, float friction, float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }
}
