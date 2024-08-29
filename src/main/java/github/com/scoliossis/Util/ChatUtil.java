package github.com.scoliossis.Util;

import github.com.scoliossis.Main;
import net.minecraft.util.ChatComponentText;

// if someone finds something wrong with this code its actually over for me.
public class ChatUtil {

    public static void chat(Object message) {
        // i dont have a "§" on my keyboard, easier to type & if i want a color code.
        if (C.isInGame()) C.p().addChatMessage(new ChatComponentText(String.valueOf(message).replaceAll("&", "§")));
    }

    public static void prefixMessage(Object message) {
        message = "&c[&f"+ Main.clientName +"&c] " + message;
        if (C.isInGame()) C.p().addChatMessage(new ChatComponentText(String.valueOf(message).replaceAll("&", "§")));
    }
}
