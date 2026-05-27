package com.gugas749.abyssitems.items.weapons;

import com.gugas749.abyssitems.Abyssitems;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class MarshmelloKnifeModel extends DefaultedItemGeoModel<MarshmelloKnifeItem> {
    public MarshmelloKnifeModel() {
        super(ResourceLocation.fromNamespaceAndPath(Abyssitems.MODID, ""));
    }

    @Override
    public ResourceLocation getModelResource(MarshmelloKnifeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(Abyssitems.MODID, "geo/item/marshmello_knife_geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarshmelloKnifeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(Abyssitems.MODID, "textures/item/marshmello_knife.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarshmelloKnifeItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(Abyssitems.MODID, "animations/item/marshmello_knife.animation.json");
    }
}
