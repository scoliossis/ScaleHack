package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Modules.Player.Disabler;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import net.minecraft.client.Minecraft;

public class NickHider {

    public static String fixText(String text) {
        if (Configs.nickHider) text = text.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), "scale");
        if (Configs.antiRacism && Configs.disabler) {
            // patches racism. donald trump should try this
            for (String word : Disabler.racistWords) {
                text = text.replaceAll(word, Disabler.censorRacism(word));
            }

            // checks for hidden racism (genuinely noticeably slows down game fps)
            //for (String word : text.split(" ")) {
            //    if (Disabler.racistWords.contains(removeBad(word)))
            //        text = text.replaceAll(word, Disabler.censorRacism(word));
            //}
        }

        return text;
    }


    public static String removeBad(String text) {
        return text.replaceAll("[.,/\\\\!@#$%^&*()]", "").toLowerCase();
    }
}
