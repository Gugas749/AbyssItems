package com.gugas749.abyssitems.items.weapons;

import com.gugas749.abyssitems.items.utils.AbyssRarities;
import com.gugas749.abyssitems.items.utils.WeaponsTiers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class MarshmelloKnifeItem extends SwordItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MarshmelloKnifeItem() {
        super(
                WeaponsTiers.MARSHMELLO,
                new Item.Properties()
                        .fireResistant()
                        .rarity(AbyssRarities.MARSHMELLO_RARITY.getValue())
                        .attributes(SwordItem.createAttributes(WeaponsTiers.MARSHMELLO, 16, 1.1F))
        );
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private MarshmelloKnifeRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new MarshmelloKnifeRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("weapons.abyssitems.knife");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

