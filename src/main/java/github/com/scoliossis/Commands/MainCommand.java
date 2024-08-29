package github.com.scoliossis.Commands;

import github.com.scoliossis.Events.PacketEvent;
import github.com.scoliossis.Main;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Screens.ClickGUI.Settings.Setting;
import github.com.scoliossis.Util.C;
import github.com.scoliossis.Util.ChatUtil;
import github.com.scoliossis.Util.SessionUtil;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// friend said most clients make a seperate file for each command
// idk why, im putting all commands in 1 file
// seperate files maybe if the function is long
public class MainCommand {

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send.Pre event) {
        if (event.packet instanceof C01PacketChatMessage) {
            String message = ((C01PacketChatMessage) event.packet).getMessage();

            if (message.startsWith(".")) {
                if (message.length() > 1) {
                    event.setCanceled(true);
                    handleCommand(message.substring(1));
                }
            }
        }
    }

    private void handleCommand(String m) {
        String[] c = m.split(" ");

        // maybe ill add fuzzysearch. idk
        if (c[0].equalsIgnoreCase("gui")) Configs.clickGUI = true;
        else if (c[0].equalsIgnoreCase("toggle")) handleToggleCommand(c);
        else if (c[0].equalsIgnoreCase("login")) handleLoginCommand(c);
        else if (c[0].equalsIgnoreCase("session")) handleSessionCommand(c);
        else sendHelpMessage(m);
    }

    public void handleSessionCommand(String[] c) {
        ChatUtil.chat(C.mc.getSession().getSessionID());
    }


    public void handleLoginCommand(String[] c) {
        if (c.length == 2) {
            SessionUtil.setSession(new Session(c[1], "7", "7", "mojang"));
            ChatUtil.prefixMessage("&aSuccessfully Logged Into Cracked Account \"&2"+c[1]+"&a\"");
        }
        else
            ChatUtil.prefixMessage("&cIncorrect usage of &6.login&c, try &a\".login [account name]\"");
    }

    public void handleToggleCommand(String[] c) {
        if (c.length > 0) {
            String q = c[1];
            List<String> mods = Main.Settings.stream().filter(mod -> mod.type == Setting.Type.BOOLEAN).map(mod -> mod.name).collect(Collectors.toList());

            // i love fuzzysearch.
            // might add tags to the modules so its more accurate but idk
            Optional<ExtractedResult> best = FuzzySearch.extractAll(q, mods, 40).stream().max(Comparator.comparingInt(ExtractedResult::getScore));
            if (!best.isPresent()) {
                ChatUtil.prefixMessage(q + " is NOT real cuh");

                return;
            }

            Config setting = (Config) Main.Settings.stream().filter(mod -> mod.type == Setting.Type.BOOLEAN).toArray()[best.get().getIndex()];

            System.out.println(setting.name + " has been tracked down.");
            setting.set(!(boolean) setting.get(Boolean.class));

            boolean enabled = (boolean) setting.get(Boolean.class);

            ChatUtil.prefixMessage((enabled ? "&aEnabled " : "&cDisabled ") + "&6" + setting.name);

        }
        else {
            // @chatgpt, generate me an appropriate error message for a misuse of the .toggle command
            ChatUtil.prefixMessage("&cIncorrect usage of &6.toggle&c, try &a\".toggle [module name]\"");
        }
    }

    public void sendHelpMessage(String m) {
        ChatUtil.chat("\n");
        ChatUtil.prefixMessage("&cInvalid Command: &4" + m);
        // i miss java 15, """ carries
        ChatUtil.prefixMessage("&4Valid Commands: ");
        ChatUtil.prefixMessage("&9.gui &3To Open The Click GUI");
        ChatUtil.prefixMessage("&9.toggle [module] &3To Toggle A Module");
        ChatUtil.chat("\n");

        // im aware i could do. looks blehhh tho
        //"&4Help\n&9.gui &3To Open The Click GUI\n&9.toggle [module] to toggle a module"
    }
}
