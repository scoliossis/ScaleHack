package github.com.scoliossis.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MotionEvent extends Event {

    @Cancelable
    public static class Pre extends Event {

        public boolean sprinting;
        public boolean sneaking;

        public double x;
        public double y;
        public double z;

        public float yaw;
        public float pitch;

        public boolean onGround;

        public Pre(boolean sprinting, boolean sneaking, double x, double y, double z, float yaw, float pitch, boolean onGround) {
            this.sprinting = sprinting;
            this.sneaking = sneaking;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
        }
    }

    public static class Post extends Event {
        public boolean sprinting;
        public boolean sneaking;

        public double x;
        public double y;
        public double z;

        public float yaw;
        public float pitch;

        public boolean onGround;

        public Post(boolean sprinting, boolean sneaking, double x, double y, double z, float yaw, float pitch, boolean onGround) {
            this.sprinting = sprinting;
            this.sneaking = sneaking;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            this.onGround = onGround;
        }
    }
}