package github.com.scoliossis.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MovementInputEvent extends Event {
    public float strafe;
    public float forward;
    public boolean jump;
    public boolean sneak;
    public float sneakSpeed;

    public MovementInputEvent(float strafe, float forward, boolean jump, boolean sneak, float sneakSpeed) {
        this.strafe = strafe;
        this.forward = forward;
        this.jump = jump;
        this.sneak = sneak;
        this.sneakSpeed = sneakSpeed;
    }
}
