package github.com.scoliossis.Events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class KeyPressEvent extends Event {
    public int key;
    public char aChar;

    public KeyPressEvent(int key, char aChar) {
        this.key = key;
        this.aChar = aChar;
    }

    public static class Pressed extends KeyPressEvent {
        public Pressed(int key, char aChar) {
            super(key, aChar);
        }
    }

    public static class Released extends KeyPressEvent {
        public Released(int key, char aChar) {
            super(key, aChar);
        }
    }
}