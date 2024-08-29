package github.com.scoliossis.Screens.ClickGUI.Settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// thank you intellij you a real one
@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {
    String name();

    // make sure tag and parent have a different default
    String tags() default "defaultTag";
    String description() default "";

    String parent() default "";

    Type type();

    float min() default 0f;
    float max() default 3f;
    int decimals() default 2;
    int[] modeRequirement() default {};

    int index() default -1;

    String[] mode() default {};

    enum Type {
        CATEGORY,

        // wtf is a boolean bro stop making up words :skull:
        BOOLEAN,

        // sub settings!
        SLIDER,
        SUBCATEGORY,
        MODE,


        // extra settings with 1 time use. (hide from arraylist and only if keybind held)
        EXTRA,
        // keybinds my beloved
        KEYBIND,

        CONFIG
    }
}
