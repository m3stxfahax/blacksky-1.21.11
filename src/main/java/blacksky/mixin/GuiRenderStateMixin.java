package blacksky.mixin;

import blacksky.access.GuiRenderStateLayerAccessor;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderState.class)
public abstract class GuiRenderStateMixin implements GuiRenderStateLayerAccessor {
    @Unique
    private int blacksky$layerSerial;

    @Override
    public int blacksky$getLayerSerial() {
        return blacksky$layerSerial;
    }

    @Inject(method = "nextStratum", at = @At("RETURN"))
    private void blacksky$trackNextStratum(CallbackInfo ci) {
        blacksky$layerSerial++;
    }

    @Inject(method = "up", at = @At("RETURN"))
    private void blacksky$trackUpLayer(CallbackInfo ci) {
        blacksky$layerSerial++;
    }

    @Inject(method = "reset", at = @At("HEAD"))
    private void blacksky$resetLayerSerial(CallbackInfo ci) {
        blacksky$layerSerial = 0;
    }
}
