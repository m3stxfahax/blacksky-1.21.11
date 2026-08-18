package blacksky.mixin.accessor;

import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.KnownPacksManager;
import net.minecraft.client.multiplayer.RegistryDataCollector;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientConfigurationPacketListenerImpl.class)
public interface ClientConfigurationPacketListenerAccessor {
    @Accessor("registryDataCollector")
    RegistryDataCollector blacksky$getRegistryDataCollector();

    @Accessor("receivedRegistries")
    RegistryAccess.Frozen blacksky$getReceivedRegistries();

    @Accessor("enabledFeatures")
    FeatureFlagSet blacksky$getEnabledFeatures();

    @Accessor("knownPacks")
    KnownPacksManager blacksky$getKnownPacks();

    @Accessor("chatState")
    ChatComponent.State blacksky$getChatState();
}
