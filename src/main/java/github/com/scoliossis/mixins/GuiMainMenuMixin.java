package github.com.scoliossis.mixins;

import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Screens.MainMenu.MainMenuMain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin extends GuiScreen implements GuiYesNoCallback {
    @Inject(method="initGui", at= @At(value="RETURN"))
    private void initGui(CallbackInfo callbackInfo) {
        if (Configs.coolMainMenu) this.mc.displayGuiScreen(new MainMenuMain());
    }
}
