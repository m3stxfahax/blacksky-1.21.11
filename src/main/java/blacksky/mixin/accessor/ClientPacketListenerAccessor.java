package blacksky.mixin.accessor;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ClientPacketListener.class)
public interface ClientPacketListenerAccessor {
    @Accessor("level")
    void blacksky$setLevel(ClientLevel level);

    @Accessor("levelData")
    void blacksky$setLevelData(ClientLevel.ClientLevelData levelData);

    @Accessor("levelData")
    ClientLevel.ClientLevelData blacksky$getLevelData();

    @Accessor("serverChunkRadius")
    void blacksky$setServerChunkRadius(int serverChunkRadius);

    @Accessor("serverChunkRadius")
    int blacksky$getServerChunkRadius();

    @Accessor("serverSimulationDistance")
    void blacksky$setServerSimulationDistance(int serverSimulationDistance);

    @Accessor("serverSimulationDistance")
    int blacksky$getServerSimulationDistance();

    @Accessor("levels")
    void blacksky$setLevels(Set<ResourceKey<Level>> levels);
}
