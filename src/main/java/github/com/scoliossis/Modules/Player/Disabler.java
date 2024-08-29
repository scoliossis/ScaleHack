package github.com.scoliossis.Modules.Player;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

/*
For those of you that don't know, there are a new pair of boots in the game called Great Spook Boots.

Hypixel, are you honestly not aware that "Spook" is a racist slur for a black person? You really couldn't call them "Spooky" or "Halloween" or "Pumpkin" or literally anything else?!

Don't believe me? Too lazy to google it? Here's the wiki page for the word. Scroll down to Slang uses.
Slang for someone involved in espionage
CIA members, in slang
MI5 members, in slang
A racial slur for a black person

A+ patch guys. Really impressed with how little you pay attention to what you put into the game.

https://hypixel.net/threads/new-great-spook-boots-contain-a-racial-slur-great-job-hypixel.4630582/
shoutout to my man corklops
 */
public class Disabler {


    // ----------------------------------------------------------------
    // most important disabler, racism.

    // obviously i cant type these words out so i split them in half !!!!!! (then asked chat gpt to say each half)
    static String[] racismList = new String[] {
            "nig", "ger",
            "nig", "ga",
            "spo", "ok",
            "co", "on",
            "jig", "ga",
            "fag", "got",
            "mon", "key",
            "rac", "ism",

            "che", "at",
            "ha", "ck",

            // truly disheartening words. (mushroom client is BANNED from this client.)
            "mush", "room",
            "wur", "st",
            "sca", "le",
            "sw", "ig",
            "bat", "man",
            "side", "load",
            "sund", "er"
    };
    public static ArrayList<String> racistWords = new ArrayList<>();

    static {
        for (int i = 0; i < racismList.length; i+=2) {
            racistWords.add(racismList[i]+racismList[i+1]);
        }
    }


    // used in the fixText function in nickHider to censor evil racism
    public static String censorRacism(String racism) {
        return racism.charAt(0)+"**"+racism.substring(3);
        //return racism.replaceAll("[aeiouAEIOU]", "*");
    }



    @SubscribeEvent
    public void onPreMotion(MotionEvent.Pre event) {
        if (!Configs.disabler || !Configs.omniSprintDisabler) return;

        event.sprinting = false;

    }
}
