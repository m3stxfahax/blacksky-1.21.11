package blacksky.mixin;

import net.minecraft.client.gui.screens.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import blacksky.api.events.impl.DeathScreenEvent;
import blacksky.manager.Manager;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void blacksky$deathScreenTick(CallbackInfo ci) {
        Manager.postEvent(new DeathScreenEvent());
    }
}
