package blacksky.mixin.accessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Invoker("startAttack")
    boolean blacksky$startAttack();

    @Invoker("startUseItem")
    void blacksky$startUseItem();

    @Invoker("updateLevelInEngines")
    void blacksky$updateLevelInEngines(ClientLevel level);

    @Accessor("rightClickDelay")
    void blacksky$setRightClickDelay(int delay);
}
