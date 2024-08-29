package github.com.scoliossis.Events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


// https://www.youtube.com/watch?v=g9xopViBKOQ

// i remember seeing a screenshot of my friends client
// which had separate files for pre and post event.
// i sadly am not that smart, maybe soon.
// theres prob a few issues w this.
// please dont point them out ur just being mean :(
public class PacketEvent extends Event {
    public Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public static class Send extends Event  {

        @Cancelable
        public static class Pre extends Event {
            public Packet<?> packet;

            public Pre(Packet<?> packet) {
                this.packet = packet;
            }
        }

        public static class Post extends Event {
            public Packet<?> packet;

            public Post(Packet<?> packet) {
                this.packet = packet;
            }
        }
    }

    public static class Receive extends Event  {

        @Cancelable
        public static class Pre extends Event {
            public Packet<?> packet;

            public Pre(Packet<?> packet) {
                this.packet = packet;
            }
        }

        public static class Post extends Event {
            public Packet<?> packet;

            public Post(Packet<?> packet) {
                this.packet = packet;
            }
        }
    }
}