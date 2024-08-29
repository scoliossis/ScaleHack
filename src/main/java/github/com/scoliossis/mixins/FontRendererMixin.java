package github.com.scoliossis.mixins;

import github.com.scoliossis.Modules.Visuals.NickHider;
import github.com.scoliossis.Screens.ClickGUI.Settings.Configs;
import github.com.scoliossis.Util.C;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Shadow
    protected abstract void renderStringAtPos(final String p0, final boolean p1);

    @Shadow
    public abstract int getStringWidth(final String p0);

    @Inject(method = "renderStringAtPos", at = @At("HEAD"), cancellable = true)
    private void renderString(String text, boolean shadow, CallbackInfo ci) {
        String text2 = NickHider.fixText(text);

        if (text2 != text) {
            ci.cancel();
            this.renderStringAtPos(text2, shadow);
        }
    }

    @Inject(method = "getStringWidth", at = @At("RETURN"), cancellable = true)
    private void getStringWidth(String text, CallbackInfoReturnable<Integer> cir) {
        if (text != null) {
            String text2 = NickHider.fixText(text);

            if (text2 != text) cir.setReturnValue(this.getStringWidth(text2));
        }
    }
}