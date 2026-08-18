package blacksky.mixin.accessor;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.block.model.ItemTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface ItemLayerRenderStateAccessor {
    @Accessor("foilType")
    ItemStackRenderState.FoilType blacksky$getFoilType();

    @Accessor("transform")
    ItemTransform blacksky$getItemTransform();

    @Accessor("usesBlockLight")
    boolean blacksky$getUsesBlockLight();

    @Accessor("specialRenderer")
    SpecialModelRenderer<Object> blacksky$getSpecialRenderer();

    @Accessor("tintLayers")
    int[] blacksky$getTintLayers();
}
