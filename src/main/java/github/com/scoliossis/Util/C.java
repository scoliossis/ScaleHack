package github.com.scoliossis.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

// C for constant, C is short, not named C because i thought C.p() would be funny.
public class C {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP p() {
        return mc.thePlayer;
    }

    public static WorldClient w() {
        return mc.theWorld;
    }

    public static boolean isInGame() {
        return p() != null && w() != null;
    }

    // :nerd: "scale u can type sout/souf on intellij and it autocompletes to System.out.println(string); anyway!!!!
    // ^^^ notice how i also dont use C.out() anywhere?
    // i like cout, cout awesome sauce.
    public static void out(Object string) {
        System.out.println(string);
    }
}
