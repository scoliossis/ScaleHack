package github.com.scoliossis.Screens.ClickGUI.Settings;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.com.scoliossis.Main;
import github.com.scoliossis.Util.ChatUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static github.com.scoliossis.Screens.ClickGUI.ClickGUIScreenMain.*;

public class Config {
    public String name;
    public String tags;

    public Field field;
    public Setting.Type type;

    public Setting setting;
    public Config parent;

    public ArrayList<Config> sons = new ArrayList<>();

    public float min;
    public float max;
    public int decimals;
    public int[] modeRequirement;

    public String[] mode;

    public int index;

    public Config(Setting setting, Field field) {
        this.setting = setting;
        this.field = field;

        // i think its easier to do setting.name instead of setting.setting.name()
        // so these are all set here
        this.name = setting.name();
        this.tags = setting.tags();
        this.type = setting.type();

        this.min = setting.min();
        this.max = setting.max();
        this.decimals = setting.decimals();
        this.modeRequirement = setting.modeRequirement();

        this.mode = setting.mode();

        this.index = setting.index();
    }

    public void set(Object object) {
        // i dont like that i need a try catch for this.
        try {field.set(object.getClass(), object);} catch (IllegalAccessException ignored) {}
    }


    public void toggleCategory() {
        Integer[] set = ((Integer[]) get(Integer[].class));
        // choose to close or to open!
        if (categoryShowing) {
            category.set(new Integer[] {set[0], set[1], set[2], set[3], 0});
            startGrowing(category, -1);
            categoryShowing = false;
        }
        else {
            category.set(new Integer[] {set[0], set[1], set[2], set[3], 1});
            startGrowing(category, 1);
            categoryShowing = true;
        }
    }


    public Object get(Class clazz) {
        try {
            return clazz.cast(field.get(Object.class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getParentChain() {
        Config setting = this;
        String parentChain = setting.name;
        while (setting.parent != null) {
            setting = setting.parent;
            parentChain = setting.name + "." + parentChain;
        }

        return parentChain;
    }


    public static class keybindStuff {
        public int key;
        public char aChar;

        public keybindStuff(int key, char aChar) {
            this.key = key;
            this.aChar = aChar;
        }
    }

    public static int amountOfSettings = 0;

    // ever feel like this shit below should be in a different file?
    // at no point would i want to do setting.collect(configs.class) when i could do Config.collect(configs.class)

    // top g shi :yawn:
    public static ArrayList<Config> collect(Class configClass) {
        ArrayList<Config> settings = new ArrayList<>();

        // add all settings to list
        for (Field field : configClass.getDeclaredFields()) {
            Config setting = new Config(field.getAnnotation(Setting.class), field);

            setting.index = amountOfSettings;
            amountOfSettings++;

            settings.add(setting);
        }

        // create extra arrays with the right size.
        Configs.keybinds = new keybindStuff[amountOfSettings];
        Configs.hideFromArray = new boolean[amountOfSettings];
        Configs.onlyWhenHeld = new boolean[amountOfSettings];

        // set parents
        for (Config setting : settings) {
            // get parent setting from parent string
            setting.parent = getSetting(setting.setting.parent(), settings);

            // adds setting to the parents, sons. i need this for gui
            if (setting.parent != null) setting.parent.sons.add(setting);
        }

        // add in extra settings
        Config keybind = getKeybindSetting(settings);
        Config holdkeybind = getHoldKeybindSetting(settings);
        Config hideSetting = getHideSetting(settings);
        for (Config setting : settings)
            // adds keybind + extra setting to all general settings
            if (setting.parent != null && setting.parent.parent == null && setting.type == Setting.Type.BOOLEAN) {
                setting.sons.add(hideSetting);
                setting.sons.add(holdkeybind);
                setting.sons.add(keybind);
            }

        // successfully collected all configuration settings. report back to homebase.
        return settings;
    }


    // config save messages stolen from mushroom, i made these anyway, the configs are saved differently tho
    static String[] sillyMessages = {
            "mushroom client configs!!! (shoutout to scale)",
            "112batman? more like 112BLACKMAN!!!! LOL",
            "rise on top long live the wood",
            "swig addons private on top :pray:",
            "i love lesbian anime girls",
            "welcome to the configs folder, enjoy ur stay!",
            "hello there!!!! ily <3",
            "hmu bud, t.me/escamas1337",
            "is this the best config system ever?",
            "who wanna kiss boys w me?",
            "*swings pendulum* \"you will let me lick your feet\"",
            "i got beamed by scale1337 :pray:",
            "yeah i stole these config save messages, shhhhh"
    };


    private static final String configFilePath = "config/" + Main.clientName2 + "/configs/";
    public static File configsFileBase = new File(configFilePath);

    public static void save(String fileName) {
        // resets loaded config
        removeOldConfig();

        configsFileBase.mkdirs();


        String sillyMessage = sillyMessages[(int) (Math.random() * sillyMessages.length)];
        StringBuilder configs = new StringBuilder("[LOADED] " + sillyMessage + "\n");
        System.out.println("saved configs with note \"" + sillyMessage + "\" ;3");

        // sort alphabetically, why not
        Main.Settings.sort(Comparator.comparing(Config::getParentChain));

        for (Config setting : Main.Settings) {

            switch (setting.type) {
                case CATEGORY:
                    // making sure the config file looks cute ;3
                    configs.append("\n\n---------------").append(setting.name.toUpperCase()).append("---------------\n");
                    configs.append("|").append(setting.getParentChain()).append(":").append(Arrays.toString((Integer[]) setting.get(Integer[].class))).append("|\n");
                    break;
                case BOOLEAN:
                case SUBCATEGORY:
                    if (setting.parent != null && setting.parent.parent == null) configs.append("\n");
                    configs.append("|").append(setting.getParentChain()).append(":").append(setting.get(Boolean.class)).append("|");
                    if (setting.parent != null && setting.parent.parent == null) {
                        configs.append("arraylisthide:").append(Configs.hideFromArray[setting.index]).append("|");
                        configs.append("onlywhenheld:").append(Configs.onlyWhenHeld[setting.index]).append("|");

                        keybindStuff keybindInfo = Configs.keybinds[setting.index];
                        configs.append("keybind:");
                        if (keybindInfo == null) configs.append("null|");
                        else configs.append(keybindInfo.key).append("#").append(keybindInfo.aChar).append("|");
                    }

                    configs.append("\n");
                    break;
                case SLIDER:
                    configs.append("|").append(setting.getParentChain()).append(":").append(setting.get(Float.class)).append("|\n");
                    break;
                case MODE:
                    configs.append("|").append(setting.getParentChain()).append(":").append(setting.get(Integer.class)).append("|\n");
                    break;
            }
        }

        // not a smart way to store these... (the index in the array changes when new booleans are added)
        //configs.append("\n\n---------------EXTRAS---------------\n");
        //configs.append("|hide from arraylist array:").append(Arrays.toString(Configs.hideFromArray)).append("|\n\n");
        //configs.append("|only when keybind held array:").append(Arrays.toString(Configs.onlyWhenHeld)).append("|\n\n");
        //configs.append("|keybinds array:[");
        //Arrays.stream(Configs.keybinds).forEach(e -> {
        //    if (e == null) configs.append("null, ");
        //    else configs.append(e.key).append("#").append(e.aChar).append(", ");
        //});
        //configs.append("]|");


        try {
            Files.write(new File(configFilePath + fileName).toPath(), configs.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(String fileName) {
        if (new File(configFilePath + fileName).exists()) {
            try {
                configsFileBase.mkdirs();

                String configFile = Files.readAllLines(new File(configFilePath + fileName).toPath()).toString();

                for (Config setting : Main.Settings) {
                    String settingPath = setting.getParentChain();
                    String searchString = "|" + settingPath + ":";

                    if (configFile.contains(searchString)) {
                        int startPoint = configFile.indexOf(searchString) + searchString.length();
                        String textAfterSettingName = configFile.substring(startPoint);
                        int endPoint = textAfterSettingName.indexOf("|");

                        String settingString = configFile.substring(startPoint, startPoint + endPoint);

                        switch (setting.type) {
                            case CATEGORY:
                                settingString = settingString.replace("[", "").replace("]", "");

                                String[] splitNumbers = settingString.split(", ");
                                Integer[] settingArray = new Integer[5];

                                for (int i = 0; i < splitNumbers.length; i++)
                                    settingArray[i] = Integer.parseInt(splitNumbers[i]);

                                setting.set(settingArray);

                                break;

                            case BOOLEAN:
                            case SUBCATEGORY:
                                setting.set(settingString.equals("true"));

                                // dumb way to load keybinds + extras. could prob shrink this code by 3x less lines
                                if (setting.parent != null && setting.parent.parent == null) {
                                    // start from end of boolean
                                    // (15 is the length of |arraylisthide:)
                                    int newStartPoint = startPoint+endPoint+15;
                                    String newTextAfterSettingName = configFile.substring(newStartPoint);
                                    // find first stopping sign thingy
                                    int newEndPoint = newTextAfterSettingName.indexOf("|");

                                    // text inbetween start and end point
                                    String hideFromArraylist = configFile.substring(newStartPoint, newStartPoint+newEndPoint);
                                    boolean hideAL = hideFromArraylist.equals("true");
                                    Configs.hideFromArray[setting.index] = hideAL;


                                    // get only when held
                                    // 14 is |onlywhenheld:
                                    newStartPoint+=hideFromArraylist.length() + 14;
                                    newTextAfterSettingName = configFile.substring(newStartPoint);
                                    newEndPoint = newTextAfterSettingName.indexOf("|");

                                    String onlyWhenHeldText = configFile.substring(newStartPoint, newStartPoint+newEndPoint);
                                    boolean onlyWhenHeld = onlyWhenHeldText.equals("true");
                                    Configs.onlyWhenHeld[setting.index] = onlyWhenHeld;


                                    // get the keybind
                                    // 9 is length of |keybind:
                                    newStartPoint+=onlyWhenHeldText.length() + 9;

                                    newTextAfterSettingName = configFile.substring(newStartPoint);
                                    newEndPoint = newTextAfterSettingName.indexOf("|");

                                    String keybindText = configFile.substring(newStartPoint, newStartPoint+newEndPoint);

                                    if (!keybindText.equals("null")) {
                                        System.out.println(keybindText);
                                        int keybindInt = Integer.parseInt(keybindText.split("#")[0]);
                                        char keybindChar = keybindText.split("#")[1].charAt(0);

                                        Configs.keybinds[setting.index] = new keybindStuff(keybindInt, keybindChar);
                                    }
                                    else Configs.keybinds[setting.index] = null;

                                }
                                break;
                            case SLIDER:
                                setting.set(Float.parseFloat(settingString));
                                break;
                            case MODE:
                                setting.set(Integer.parseInt(settingString));
                                break;
                        }
                    }
                }

                Main.selectedConfig = fileName;

                // this was dumb + made everything harder
                //Configs.hideFromArray = getBooleanArray("hide from arraylist array", configFile);
                //Configs.onlyWhenHeld = getBooleanArray("only when keybind held array", configFile);
                //
                //Configs.keybinds = getKeybinds("keybinds array", configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // saves config that was just loaded, completely unnecessary p much, just here to add the [LOADED] tag to the top of files
        save(fileName);
    }


    public static void removeOldConfig() {
        configsFileBase.mkdirs();

        // for loop incase they have multiple loaded
        for (File file : configsFileBase.listFiles()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                while (reader.ready()) {
                    content.append(reader.readLine()).append("\n");
                }
                reader.close();
                content.toString();

                String oldText = content.toString();
                if (oldText.contains("[LOADED] ")) {
                    oldText = oldText.replaceAll("\\[LOADED] ", "");
                    Files.write(file.toPath(), oldText.getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // yes there are easier ways to have the last loaded config stored, eg a file just containing the name of last loaded file
    // however having [LOADED] at the start of the file is cooler.
    public static String getLastLoadedConfig() {
        if (configsFileBase.isDirectory()) {
            for (File file : Objects.requireNonNull(configsFileBase.listFiles())) {
                try {
                    String fileText = Files.readAllLines(file.toPath()).toString();
                    if (fileText.contains("[LOADED]")) {
                        System.out.println("found config " + file.getName() + " which was previously loaded");
                        return file.getName();
                    }
                } catch (IOException e) {
                    System.out.println("epic fail");
                }
            }
        }

        return "config.json";
    }



    public static Config getKeybindSetting(ArrayList<Config> Configs) {
        for (Config setting : Configs) if (setting.type == Setting.Type.KEYBIND) return setting;

        return null;
    }

    public static Config getHideSetting(ArrayList<Config> Configs) {
        for (Config setting : Configs) if (Objects.equals(setting.name, "Hide")) return setting;

        return null;
    }

    public static Config getHoldKeybindSetting(ArrayList<Config> Configs) {
        for (Config setting : Configs) if (Objects.equals(setting.name, "Hold Keybind")) return setting;

        return null;
    }



    public static Config getSetting(String configName, ArrayList<Config> Configs) {
        // loop through settings until a setting with the tag of the parent is found!
        for (Config setting : Configs) if (setting.tags.equals(configName)) return setting;

        // if the tag isnt found, loop names, makes stuff easier
        // loop through settings until a setting with the name of the parent is found!
        for (Config setting : Configs) if (setting.name.equals(configName)) return setting;

        return null;
    }

    public static Config highestParent(Config config) {
        while (config.parent != null) config = config.parent;

        return config;
    }

    public static int getCategorys(ArrayList<Config> settings) {
        return (int) settings.stream().filter(setting -> setting.type == Setting.Type.CATEGORY).count();
    }


    // TODO: save and load configs. do NOT do something autistic istg.
}
