package com.gugas749.abyssitems.items.registrie;

import com.gugas749.abyssitems.Abyssitems;
import com.gugas749.abyssitems.items.weapons.MarshmelloKnifeItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Abyssitems.MODID);

    /*
     *** WEAPONS
     */
    public static final DeferredHolder<Item, Item> MARSHMELLO_KNIFE = ITEMS.register
            ("marshmello_knife", MarshmelloKnifeItem::new);




    /*
     *** REGISTER
     */

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
