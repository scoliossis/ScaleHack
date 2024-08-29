package github.com.scoliossis.Screens.ClickGUI.Settings;

public class Configs {
    // put the modules here!
    // every single one of them.
    // in a single file.
    // is this suboptimal?


    // COMBAT
    @Setting(type = Setting.Type.CATEGORY, name = "Combat")
    // x / y is the first 2 ints
    // third int should be which index the category is, combat 0, visuals 1, etc. dumb im sowwy
    // color is set as the fourth.
    // 5th is 1 to show, 0 to hide
    public static Integer[] combat = new Integer[] {10,10, 0, 0xE84C3C, 1};

    @Setting(type = Setting.Type.BOOLEAN, name = "Kill Aura", parent = "Combat")
    public static boolean killAura = false;

    @Setting(type = Setting.Type.SUBCATEGORY, name = "Range", parent = "Kill Aura")
    public static boolean rangeSubcategory = false;
    @Setting(type = Setting.Type.SLIDER, name = "Rotation Range", parent = "Range", min = 1, max = 8)
    public static float killAuraRotationRange = 5f;
    @Setting(type = Setting.Type.SLIDER, name = "Attack Range", parent = "Range", min = 1, max = 6)
    public static float killAuraAttackRange = 3.1f;
    @Setting(type = Setting.Type.SLIDER, name = "Through Walls Range", parent = "Range", min = 0, max = 6)
    public static float killAuraWallsRange = 2f;


    @Setting(type = Setting.Type.SUBCATEGORY, name = "Targeting", parent = "Kill Aura")
    public static boolean killAuraTargetingSubCategory = false;
    @Setting(type = Setting.Type.MODE, name = "Target Sorting", parent = "Targeting", mode = {"Distance", "Hurt Time", "Health"})
    public static int killAuraSorting = 0;
    // maybe add multi idk
    @Setting(type = Setting.Type.MODE, name = "Target Switching", parent = "Targeting", mode = {"Switch", "Best", "Single"})
    public static int killAuraTarget = 0;


    @Setting(type = Setting.Type.SUBCATEGORY, name = "Rotation", parent = "Kill Aura")
    public static boolean rotationsSubcategory = false;
    @Setting(type = Setting.Type.MODE, name = "Rotations", parent = "Rotation", mode = {"Simple", "Smooth", "Snap", "None"})
    public static int rotations = 0;
    @Setting(type = Setting.Type.SLIDER, name = "Min Rotation", parent = "Rotations", modeRequirement = {0}, min = 0, max = 180)
    public static float minRotation = 0;
    @Setting(type = Setting.Type.SLIDER, name = "Max Rotation", parent = "Rotations", modeRequirement = {0}, min = 1, max = 180)
    public static float maxRotation = 100;
    @Setting(type = Setting.Type.SLIDER, name = "Rotation Smoothing", parent = "Rotations", modeRequirement = {1}, min = 1f, max = 5)
    public static float smoothRotationSpeed = 1;


    @Setting(type = Setting.Type.SUBCATEGORY, name = "Attacking", parent = "Kill Aura")
    public static boolean attackingSubCat = false;
    @Setting(type = Setting.Type.BOOLEAN, name = "Keep Sprint", parent = "Attacking")
    public static boolean auraKeepSprint = true;
    @Setting(type = Setting.Type.BOOLEAN, name = "Sword Only", parent = "Attacking")
    public static boolean swordOnlyAura = true;

    @Setting(type = Setting.Type.SLIDER, name = "Attack Speed Min", parent = "Attacking", min = 0, max = 20)
    public static float killAuraAttackSpeedMin = 5f;
    @Setting(type = Setting.Type.SLIDER, name = "Attack Speed Max", parent = "Attacking", min = 0, max = 20)
    public static float killAuraAttackSpeedMax = 10f;


    @Setting(type = Setting.Type.BOOLEAN, name = "Velocity", parent = "Combat")
    public static boolean velocity = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Criticals", parent = "Combat")
    public static boolean criticals = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Reach", parent = "Combat")
    public static boolean reach = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Aim Assist", parent = "Combat")
    public static boolean aimassist = false;




    // MOVEMENT
    @Setting(type = Setting.Type.CATEGORY, name = "Movement")
    public static Integer[] movement = new Integer[] {160,10, 1, 0x22B871, 1};

    @Setting(type = Setting.Type.BOOLEAN, name = "Speed", parent = "Movement")
    public static boolean speed = false;
    @Setting(type = Setting.Type.MODE, name = "Speed Mode", parent = "Speed", mode = {"vanilla", "groundstrafe", "friction"})
    public static int speedMode = 0;
    @Setting(type = Setting.Type.SLIDER, name = "Speed Speed", parent = "Speed Mode", modeRequirement = {0}, min = 0.01f, max = 3f)
    public static float speedSpeed = 0.1f;
    @Setting(type = Setting.Type.SLIDER, name = "Friction", parent = "Speed Mode", modeRequirement = {2}, min = 0.01f, max = 1f)
    public static float speedFriction = 0.2f;

    @Setting(type = Setting.Type.BOOLEAN, name = "No Fall", parent = "Movement")
    public static boolean noFall = false;
    @Setting(type = Setting.Type.MODE, name = "Mode", tags = "nofallmode", parent = "No Fall", mode = {"No Ground", "Ground Spoof", "Packet", "Hypixel", "Vulcan"})
    public static int noFallMode = 0;
    @Setting(type = Setting.Type.MODE, name = "Hypixel Mode", parent = "nofallmode", modeRequirement = {3}, mode = {"Packet", "Dog", "Jump"})
    public static int hypixelNoFallMode = 0;


    @Setting(type = Setting.Type.BOOLEAN, name = "Flight", parent = "Movement")
    public static boolean flight = false;
    // min = -1 :trol:
    @Setting(type = Setting.Type.SLIDER, name = "Horizontal Speed", parent = "Flight", min = -1, max = 2)
    public static float flightHorizontalSpeed = 0.5f;

    @Setting(type = Setting.Type.SLIDER, name = "Vertical Speed", parent = "Flight", min = -1, max = 2)
    public static float flightVerticalSpeed = 0.5f;


    @Setting(type = Setting.Type.BOOLEAN, name = "Long Jump", parent = "Movement")
    public static boolean longJump = false;


    @Setting(type = Setting.Type.BOOLEAN, name = "No Slowdown", parent = "Movement")
    public static boolean noSlow = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Sprint", parent = "Movement")
    public static boolean sprint = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Inventory Walk", parent = "Movement")
    public static boolean invMove = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Eagle", parent = "Movement")
    public static boolean eagle = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Step", parent = "Movement")
    public static boolean step = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "No Clip", parent = "Movement")
    public static boolean noClip = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Only Down If Crouching", parent = "No Clip")
    public static boolean noClipOnlyCrouchDown = false;


    // PLAYER
    @Setting(type = Setting.Type.CATEGORY, name = "Player")
    public static Integer[] player = new Integer[] {310,10, 2, 0x9352B0, 1};

    @Setting(type = Setting.Type.BOOLEAN, name = "Scaffold", parent = "Player")
    public static boolean scaffold = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Fast Break", parent = "Player")
    public static boolean fastbreak = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Inventory Manager", parent = "Player")
    public static boolean inventoryManager = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Blink", parent = "Player")
    public static boolean blink = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Fucker", parent = "Player")
    public static boolean fucker = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Stealer", parent = "Player")
    public static boolean stealer = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Timer", parent = "Player")
    public static boolean timer = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Auto Tool", parent = "Player")
    public static boolean autoTool = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Auto Play", parent = "Player")
    public static boolean autoplay = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Disabler", parent = "Player")
    public static boolean disabler = false;
    @Setting(type = Setting.Type.BOOLEAN, name = "Racism Disabler", parent = "Disabler")
    public static boolean antiRacism = true;
    @Setting(type = Setting.Type.BOOLEAN, name = "Omni Sprint Disabler", parent = "Disabler")
    public static boolean omniSprintDisabler = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "Movement Fix", parent = "Player")
    public static boolean moveFix = false;
    @Setting(type = Setting.Type.MODE, name = "Mode", parent = "Movement Fix", mode = {"Normal", "Awesome"})
    public static int moveFixMode = 0;

    @Setting(type = Setting.Type.BOOLEAN, name = "Correct GCD", parent = "Player")
    public static boolean correctGCD = false;
    @Setting(type = Setting.Type.SLIDER, name = "Divisor", parent = "Correct GCD", min = 0.5f, max = 5f)
    public static float correctGCDnumber = 2f;


    @Setting(type = Setting.Type.BOOLEAN, name = "Fast Place", parent = "Player")
    public static boolean fastPlace = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Anti Aim", parent = "Player")
    public static boolean antiAim = false;
    @Setting(type = Setting.Type.BOOLEAN, name = "Server-Side", parent = "Anti Aim")
    public static boolean serverSideAntiAim = false;

    // Skyblack
    @Setting(type = Setting.Type.CATEGORY, name = "Skyblock")
    public static Integer[] skyblock = new Integer[] {460,10, 3, 0xFD9C0C, 1};

    @Setting(type = Setting.Type.BOOLEAN, name = "Pelt Helper", parent = "Skyblock")
    public static boolean peltHelper = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Bar Phase", parent = "Skyblock")
    public static boolean barPhase = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Ghost Blocks", parent = "Skyblock")
    public static boolean gKey = false;

    @Setting(type = Setting.Type.SLIDER, name = "Ghost Block Reach", parent = "Ghost Blocks", min = 1, max = 10)
    public static float gKeyReach = 6f;

    @Setting(type = Setting.Type.SLIDER, name = "Ghost Block Delay", parent = "Ghost Blocks", min = 0, max = 5, decimals = 0)
    public static float gKeyDelay = 2;

    @Setting(type = Setting.Type.BOOLEAN, name = "Ghost Block Whitelist", parent = "Ghost Blocks")
    public static boolean gKeyWhitelist = true;


    // VISUALS
    @Setting(type = Setting.Type.CATEGORY, name = "Visuals")
    public static Integer[] visuals = new Integer[] {610,10, 4, 0x3600CE, 1};

    @Setting(type = Setting.Type.BOOLEAN, name = "ESP", parent = "Visuals")
    public static boolean esp = false;
    @Setting(type = Setting.Type.BOOLEAN, name = "Chams", parent = "ESP")
    public static boolean chams = false;
    @Setting(type = Setting.Type.MODE, name = "Chams Mode", parent = "Chams", mode = {"Offset", "Depth"})
    public static int chamsMode = 0;

    @Setting(type = Setting.Type.BOOLEAN, name = "Ghost Chams", parent = "Chams")
    public static boolean ghostChams = false;
    @Setting(type = Setting.Type.BOOLEAN, name = "Glow", parent = "ESP")
    public static boolean glowESP = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Block ESP", parent = "Visuals")
    public static boolean blockESP = false;


    @Setting(type = Setting.Type.BOOLEAN, name = "HUD", parent = "Visuals")
    public static boolean HUD = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "Full Bright", parent = "Visuals")
    public static boolean fullBright = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "No Effect", parent = "Visuals")
    public static boolean antiEffect = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Scoreboard", parent = "Visuals")
    public static boolean customScoreboard = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Camera", parent = "Visuals")
    public static boolean camera = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Free Look", parent = "Visuals")
    public static boolean freelook = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Array List", parent = "Visuals")
    public static boolean arrayList = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Hilarity", parent = "Visuals")
    public static boolean hilarity = true;
    @Setting(type = Setting.Type.SLIDER, name = "Hilarity Delay", parent = "Hilarity", min = 1, max = 10000)
    public static float hilarityTimer = 5000;

    @Setting(type = Setting.Type.BOOLEAN, name = "Show Rotations", parent = "Visuals")
    public static boolean showRots = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "Trail", parent = "Visuals")
    public static boolean trail = false;
    @Setting(type = Setting.Type.SLIDER, name = "Trail Length", parent = "Trail", min = 1, max = 200, decimals = 0)
    public static float trailLength = 10;

    @Setting(type = Setting.Type.BOOLEAN, name = "Name Tags", parent = "Visuals")
    public static boolean nameTags = false;

    @Setting(type = Setting.Type.BOOLEAN, name = "Nick Hider", parent = "Visuals")
    public static boolean nickHider = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "LSD", parent = "Visuals")
    public static boolean LSD = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "Cool Main Menu", parent = "Visuals")
    public static boolean coolMainMenu = true;

    @Setting(type = Setting.Type.BOOLEAN, name = "Zoom", parent = "Visuals")
    public static boolean zoom = true;


    // important part of the client! im really glad there is no $25 "chinese" client which forgot to add this for a year!
    @Setting(type = Setting.Type.BOOLEAN, name = "Click GUI", parent = "Visuals")
    public static boolean clickGUI = false;

    // CONFIGS
    @Setting(type = Setting.Type.CATEGORY, name = "Configs")
    public static Integer[] configs = new Integer[] {760,10, 5, 0x3B92D6, 1};

    @Setting(type = Setting.Type.CONFIG, name = "Create New", parent = "Configs")
    public static boolean CREATE_NEW_CONFIG = false;








    @Setting(type = Setting.Type.EXTRA, name = "Hide")
    public static boolean[] hideFromArray = new boolean[]{};
    @Setting(type = Setting.Type.EXTRA, name = "Hold Keybind")
    public static boolean[] onlyWhenHeld = new boolean[]{};

    @Setting(type = Setting.Type.KEYBIND, name = "Keybind")
    public static Config.keybindStuff[] keybinds = new Config.keybindStuff[]{};
}
