package github.com.scoliossis;

import github.com.scoliossis.Commands.MainCommand;
import github.com.scoliossis.Events.KeyPressEvent;
import github.com.scoliossis.Modules.Combat.Killaura;
import github.com.scoliossis.Modules.Movement.*;
import github.com.scoliossis.Modules.Player.MovementFix;
import github.com.scoliossis.Modules.Player.AntiAim;
import github.com.scoliossis.Modules.Player.Disabler;
import github.com.scoliossis.Modules.Player.Scaffold;
import github.com.scoliossis.Modules.Skyblock.BarPhase;
import github.com.scoliossis.Modules.Skyblock.GhostBlocks;
import github.com.scoliossis.Modules.Visuals.*;
import github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain;
import github.com.scoliossis.Screens.ClickGUI.Settings.Config;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.Mod.EventHandler;

import java.util.ArrayList;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    // idk a name for this client yet
    public static final String clientName = "Scale Hack";
    public static final String clientName2 = "ScaleHack";

    public static final String MODID = clientName2;
    public static final String VERSION = "1.0";

    public static ArrayList<Config> Settings = Config.collect(Configs.class);

    public static String selectedConfig = "";

    @EventHandler
    public void onInit(FMLPreInitializationEvent event) {
        SessionUtil.sessionLogin("eyJraWQiOiJhYzg0YSIsImFsZyI6IkhTMjU2In0.eyJ4dWlkIjoiMjUzNTQ0NDM2MTM0MTU1NCIsImFnZyI6IkFkdWx0Iiwic3ViIjoiMzk2MzEzMmUtY2FhZC00YTU2LTljMzItM2E4M2I1Mzk0MmZhIiwiYXV0aCI6IlhCT1giLCJucyI6ImRlZmF1bHQiLCJyb2xlcyI6W10sImlzcyI6ImF1dGhlbnRpY2F0aW9uIiwiZmxhZ3MiOlsidHdvZmFjdG9yYXV0aCIsIm1zYW1pZ3JhdGlvbl9zdGFnZTQiLCJvcmRlcnNfMjAyMiIsIm11bHRpcGxheWVyIl0sInByb2ZpbGVzIjp7Im1jIjoiNGEzZTQ5OWItYjIwNi00YmU4LTliZWYtODdjM2JmYzEwMWY1In0sInBsYXRmb3JtIjoiVU5LTk9XTiIsInl1aWQiOiI5M2Q1Mzk1YWM0OTFjZTVlMTJlMTAwYjVhZDgzMWFkMyIsIm5iZiI6MTcyNDg1MDkzNCwiZXhwIjoxNzI0OTM3MzM0LCJpYXQiOjE3MjQ4NTA5MzR9.X_CLKXvzRT0TIO-K9mFyBmGYhDTRqJMXvLezdfp7KHY");

        Display.setTitle(clientName + " " + VERSION);

        FontUtil.loadFont(RenderUtil.FontSansSerif, true);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MainCommand());

        // sorry for no auto register, wouldnt be hard, ill make later
        MinecraftForge.EVENT_BUS.register(new Killaura());

        MinecraftForge.EVENT_BUS.register(new BarPhase());
        MinecraftForge.EVENT_BUS.register(new GhostBlocks());

        MinecraftForge.EVENT_BUS.register(new ESP());
        MinecraftForge.EVENT_BUS.register(new HUD());
        MinecraftForge.EVENT_BUS.register(new BlockESP());
        MinecraftForge.EVENT_BUS.register(new ServerSideRotations());
        MinecraftForge.EVENT_BUS.register(new Trail());
        MinecraftForge.EVENT_BUS.register(new Nametags());
        MinecraftForge.EVENT_BUS.register(new LSD());
        MinecraftForge.EVENT_BUS.register(new FreeLook());
        MinecraftForge.EVENT_BUS.register(new Zoom());
        MinecraftForge.EVENT_BUS.register(new Hilarity());

        MinecraftForge.EVENT_BUS.register(new Flight());
        MinecraftForge.EVENT_BUS.register(new NoClip());
        MinecraftForge.EVENT_BUS.register(new Speed());
        MinecraftForge.EVENT_BUS.register(new NoFall());

        MinecraftForge.EVENT_BUS.register(new AntiAim());
        MinecraftForge.EVENT_BUS.register(new Disabler());
        MinecraftForge.EVENT_BUS.register(new MovementFix());
        MinecraftForge.EVENT_BUS.register(new Scaffold());

        System.out.println("minecraft is booting up!!!");

        selectedConfig = Config.getLastLoadedConfig();
        Config.load(selectedConfig);
        System.out.println("loaded config");
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Configs.clickGUI) {
            C.mc.displayGuiScreen(new ClickGUIScreenMain());
            Configs.clickGUI = false;
        }
    }

    @SubscribeEvent
    public void onKeyPress(KeyPressEvent event) {
        for (Config setting : Main.Settings) {
            if (setting.parent != null && setting.parent.parent == null && Configs.keybinds[setting.index] != null) {
                if (Configs.keybinds[setting.index].key == event.key) {

                    // check if key pressed
                    if (event instanceof KeyPressEvent.Pressed) {
                        setting.set(!(Boolean) setting.get(Boolean.class));
                        ChatUtil.chat(setting.name + ((Boolean) setting.get(Boolean.class) ? " enabled" : " disabled"));
                    }

                    else if (Configs.onlyWhenHeld[setting.index]) {
                        setting.set(!(Boolean) setting.get(Boolean.class));
                        ChatUtil.chat(setting.name + ((Boolean) setting.get(Boolean.class) ? " enabled" : " disabled") + " due to key release");
                    }
                }
            }
        }
    }
}