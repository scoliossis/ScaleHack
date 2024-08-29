package github.com.scoliossis.Modules.Visuals;

import github.com.scoliossis.Events.MotionEvent;
import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.ChatUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Hilarity {
    int i;
    String[] Hilaritynames = {
            "&b[MVP&c+&b] pewing",
            "&b[MVP] swig",
            "&c[ADMIN] Jayavarmen",
            "&c[ADMIN] Minikloon",
            "&c[ADMIN] Ladybleu",
            "&c[OWNER] hypixel",
            "&c[OWNER] Rezzus",
            "&c[ADMIN] Plancke",
            "&c[ADMIN] aPunch",
            "&b[MVP&0+&b] Dctr",
            "&c[ADMIN] Thorlon",
            "&c[ADMIN] Froz3n",
            "&c[ADMIN] ChiLynn",
            "&c[Minister] 2nfg",
            "&b[MVP&0+&b] Strongchaff",
            "&c[&fYOUTUBE&c] Mushroom",
            "&c[&fYOUTUBE&c] Frozie",
            "&c[&fYOUTUBE&c] Refraction",
            "&c[&fYOUTUBE&c] K9L",
            "&c[&fYOUTUBE&c] 56ms",
            "&c[&fYOUTUBE&c] AverageSweat",
            "&c[&fYOUTUBE&c] Vegantoes",
            "&c[&fYOUTUBE&c] __Zova",
            "&c[&fYOUTUBE&c] Scale",
            "&b[MVP] Alan_Wood",
            "&7kulovhax",
            "&c[&fYOUTUBE&c] ijustcheat",
            "&7oslurpie",
            "&b[MVP] &cOctoSplash",
            "&a[VIP] riel"
    };

    String[] Hilaritymessages = {
            "your gay\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*",
            "Your flagging.\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*",
            "watchdog exists",
            "your getting banned :)\n&dFrom &b[MVP&2+&b] Elg_AF&7: you're*",
            "any last words?",
            "x3 nuzzles! pounces on you uwu you so warm (ooo) Couldn’t help but notice your buldge from across the floor Nuzzles yo' necky wecky-tilda murr-tilda hehe Unzips yo baggy ass pants, oof baby you so musky Take me home, pet me, 'n’ make me yours and don't forget to stuff me! See me wag my widdle baby tail all for your buldgy-wuldgy! Kissies 'n' lickies yo neck I hope daddy likies Nuzzles 'n' wuzzles yo chest (yuh) I be (yeah) gettin' thirsty Hey, I got a little itch, you think you can help me? Only seven inches long uwu PLEASE ADOPT ME Paws on your buldge as I lick my lips (uwu punish me please) 'Boutta hit ’em with this furry shit (he don’t see it comin'). Dowoes youwu lick my sowong.",
            "you really thought that wasnt detectable?",
            "hey whats " + Main.clientName + "? ;3",
            "du kannst nichts daher ist dein vatter milch hollen",
            "t.me/escamas1337 !!",
            "https://github.com/scoliossis",
            "For those of you that don't know, there are a new pair of boots in the game called Great Spook Boots., " +
                    ", " +
                    "Hypixel, are you honestly not aware that \"Spook\" is a racist slur for a black person? You really couldn't call them \"Spooky\" or \"Halloween\" or \"Pumpkin\" or literally anything else?!, " +
                    ", " +
                    "Don't believe me? Too lazy to google it? Here's the wiki page for the word. Scroll down to Slang uses., " +
                    "Slang for someone involved in espionage, " +
                    "CIA members, in slang, " +
                    "MI5 members, in slang, " +
                    "A racial slur for a black person, " +
                    ", " +
                    "A+ patch guys. Really impressed with how little you pay attention to what you put into the game.",

            "@everyone I have decided that I am going to be leaving the Diablo development team for a few reason one of them being tanner, tanner is unable to do simple tasks such as change a variable to stop a task another reason is that the client is unstable and will crash randomly and finally is that no one on the dev team is helpful in anyway I am just left to do most things by myself.",

            "\"Unfortunate\" doesn't begin to describe my series, this game rewards blind luck and nothing else, I am beyond convinced at this point. After getting completely tooled by scheduling with my opponent changing times on me last minute and refusing to provide confirmation prior to the day of the match as to play times, losing this way somehow felt even worse than I had thought possible. My preparation was superior, my play was superior, and I lost, so I don't see a reason to continue engaging in an activity where what is within my control is overwhelmingly outweighed by what is not., " +
                    "I am done with competitive Pokemon, and you won't get a fond farewell. This community is infected to its roots with a degenerative disease that grows stronger over time but stops short of killing its host. Tournaments used to have a competitive spirit at their heart, this has been transplanted and replaced with an artificial organ that feeds on vitriol and mockery from insecure little boys that heckle by the sidelines and tear each other to shreds over scraps of attention. The environment we fostered has trapped us all like this in a vicious cycle, and escaping it requires acceptance of the harshest reality we all scramble to explain away, that none of the countless straining efforts we put ourselves through here will ever amount to one single shining glimmer of significance. I would make this the end, but World Cup is still ongoing, and I would never leave so many great friends out to dry, so I'll suffer through a few more games for them., " +
                    ", " +
                    "One last thing before I leave you all to react with disdain, ridicule, and self-righteous fervor, before you do everything in your power to minimize my words and thoughts, box them up and shove them to some cobwebbed corner of your memory, and hope they disappear forever as a stain on your finite time ground to dust. From this moment on, nothing you say matters to me. The foulest insults you hurl with intent to wound will calmly settle at the earth before my feet, and the venom you spit will bring all the pain of a warm summer breeze. You are less than anything you can conceive, while I carry on, brimming with joy distilled from detachment.",

            "Since Mushroom Client's visuals don't look the best, I'm offering everyone a chance to help improve the visuals for the client they use (or may use in the future), and have a chance at winning a license in the process. Over the course of the next week (until lunes, 3 de junio de 2024 22:59, ends in hace 3 meses) these timestamps are automatically adjusted to your timezone everyone (even if you don't own mushroom client) will be able to suggest visual designs for any category you can think of (except our website, we already have a design that'll be made into the actual website soon). To do this post your design (or multiple, you can post multiple designs in the same forum thread) in \u2060suggestions. Any of the following things are accepted:, " +
                    "Screenshots of (videos of) other clients, make sure to crop them to the specific visual, " +
                    "Text descriptions of visuals, " +
                    "HIGHLY PREFERRED Drawn mockups of the visual (with optional textual clarification), it doesn't have to be a good drawing, as long as the main idea is clear (eg. rounded corners don't have to be perfectly round)., " +
                    "Everyone can then vote designs (multiple votes allowed) they like by reacting with ⬆\uFE0F to the post in \u2060suggestions. At the end of the 2 designs with the most votes win a Mushroom Client license key, you can use this yourself, or give it away (to a friend)., " +
                    "Some examples of things you could make a design for are (you can of course also do other things):, " +
                    "ClickGUI," +
                    "TargetHUD," +
                    "Arraylist," +
                    "Main menu," +
                    "Watermark, " +
                    "Session stats, " +
                    "Hit effect, " +
                    "Jump circle, " +
                    ", " +
                    "In short, " +
                    "Visuals competition in \u2060suggestions, " +
                    "Two best designs win a Mushroom Client license key, " +
                    "Ends at lunes, 3 de junio de 2024 22:59 (that is hace 3 meses), " +
                    "Any type of submission allowed, for any part of the client except the website, " +
                    ", " +
                    "We are excited to see what you come up with! Good luck!, " +
                    "@everyone",

            "https://www.youtube.com/watch?v=_WABmx5VcyE",

            "Have you checked out mushroom client? Its the FRESHEST blatant client on mushroomer.top. You can save your accounts BLOCK the ban packets. It supports 1.8.9-1.20.4 servers, that means you can play on probably any server you can imagine. It works on windows 10 AND windows 11 as well as maybe others one day, some of you guys can't play on clients because they only work on windows but this one is the same. By default it runs on the mushroom launcher you can also play it in the vanilla minecraft launcher that means you can use your 2FA Microsoft Accounts but if you're fine with using the alt manager in mushroom client that's built-in you can also play on cookie and you can play on all microsoft accounts. This client is undetectable on basically every server and to be honest you can probably configurate it to bypass every server it's got configs came already and some stuff is in the cloud already that means if you play on 1 computer log off join on another computer you're still gonna have all your settings and everything not there., " +
                    "Chest aura is working just fine." +
                    "Lets Godbridge over." +
                    "Killaura." +
                    "Knocked him of the map." +
                    "There's already someone here, and our ESP tells us the chests have been looted, but that doesn't matter we're gonna kill this dude. awkward cut , " +
                    "gayily i can turn up my velocity if you want lil bro. , " +
                    "Alright. , " +
                    "Man down. , " +
                    "Lets godbridge over. , " +
                    "The power of mushroom baby. , " +
                    "Get em both. Janitor? , " +
                    "Lets win the game!, " +
                    ", " +
                    "Lets get some armor up gets 2 pieces of armor, " +
                    "Man he's, he's already up there. wierd ahh 2 second cut, " +
                    "im ready to fight! :mm_devil: , " +
                    "Lets get up at the top., " +
                    "Ok he thinks he tnt, you can blow yourself up bro, " +
                    "10 seconds of silence?!?!, " +
                    "alright!, " +
                    "That's how you do it!, " +
                    "So now there is just one dude left!, " +
                    "He's probably trapping?!, " +
                    "So I'm just gonna do what I'm supposed to., " +
                    "Oh he dead, " +
                    "Alright we win the game!, " +
                    "Mushroom killaura doin pretty good"
    };


    @SubscribeEvent
    public void keypressed(MotionEvent.Post event) {
        if (Configs.hilarity) {
            i++;
            if (i >= Configs.hilarityTimer) {
                ChatUtil.chat("&dFrom " + Hilaritynames[(int) Math.floor(Math.random() * Hilaritynames.length)] + "&7: " + Hilaritymessages[(int) Math.floor(Math.random() * Hilaritymessages.length)]);
                i=0;
            }
        }
    }

}
