package github.com.scoliossis.mixins;

import github.com.scoliossis.Events.KeyPressEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Unique
    ArrayList<Integer> pressedKeys = new ArrayList<>();

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        // awesome key released event real
        for (int i = 0; i < pressedKeys.size(); i++) {
            if (!Keyboard.isKeyDown(pressedKeys.get(i))) {
                MinecraftForge.EVENT_BUS.post(new KeyPressEvent.Released(pressedKeys.get(i), Keyboard.getEventCharacter()));
                pressedKeys.remove(i);
            }
        }
    }


    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V"))
    public void keyPresses(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            if (!pressedKeys.contains(Keyboard.getEventKey())) {
                MinecraftForge.EVENT_BUS.post(new KeyPressEvent.Pressed(Keyboard.getEventKey(), Keyboard.getEventCharacter()));
                pressedKeys.add(Keyboard.getEventKey());
            }
        }
    }
}
