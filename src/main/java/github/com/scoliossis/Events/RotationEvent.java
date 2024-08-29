package github.com.scoliossis.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

// rotation event! like pre motion but before movement fix!
public class RotationEvent extends Event {
    public float yaw;
    public float pitch;

    public RotationEvent(float yaw, float pitch, boolean fixMovement) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}