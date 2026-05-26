package com.gugas749.abyssitems.items.weapons;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MarshmelloKnifeRenderer extends GeoItemRenderer<MarshmelloKnifeItem> {
    public MarshmelloKnifeRenderer() {
        super(new MarshmelloKnifeModel());
    }
}
