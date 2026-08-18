package blacksky.mixin.accessor;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface MultiPlayerGameModeAccessor {
    @Accessor("isDestroying")
    void blacksky$setDestroying(boolean isDestroying);

    @Accessor("isDestroying")
    boolean blacksky$isDestroying();

    @Accessor("destroyBlockPos")
    BlockPos blacksky$getDestroyBlockPos();

    @Invoker("ensureHasSentCarriedItem")
    void blacksky$ensureHasSentCarriedItem();

    @Accessor("destroyDelay")
    void blacksky$setDestroyDelay(int destroyDelay);

    @Accessor("destroyProgress")
    float blacksky$getDestroyProgress();

    @Accessor("destroyProgress")
    void blacksky$setDestroyProgress(float destroyProgress);
}
