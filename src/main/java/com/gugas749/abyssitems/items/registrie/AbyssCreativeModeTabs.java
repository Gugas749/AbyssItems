package com.gugas749.abyssitems.items.registrie;

import com.gugas749.abyssitems.Abyssitems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AbyssCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Abyssitems.MODID);

    public static final Supplier<CreativeModeTab> ABYSS_ITEMS = CREATIVE_MODE_TAB.register("abyss_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemsRegistry.MARSHMELLO_KNIFE.get()))
                    .title(Component.translatable("creativetab.abyssitems"))
                    .displayItems((itemDisplayParameters, output) -> {
                        /*
                         *** WEAPONS
                         */

                        output.accept(ItemsRegistry.MARSHMELLO_KNIFE.get());



                        /*
                         *** ITEMS
                         */

                        output.accept(ItemsRegistry.COMPLETE_KEY.get());
                        output.accept(ItemsRegistry.TOP_PART_KEY.get());
                        output.accept(ItemsRegistry.MIDDLE_PART_KEY.get());
                        output.accept(ItemsRegistry.BOTTOM_PART_KEY.get());
                        output.accept(ItemsRegistry.VOID_ESSENCE.get());

                    }).build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
