package com.gugas749.abyssitems.items.registrie;

import com.gugas749.abyssitems.Abyssitems;
import com.gugas749.abyssitems.items.normalitems.keys.BottomPartKeyItem;
import com.gugas749.abyssitems.items.normalitems.keys.CompleteKeyItem;
import com.gugas749.abyssitems.items.normalitems.keys.MiddlePartKeyItem;
import com.gugas749.abyssitems.items.normalitems.keys.TopPartKeyItem;
import com.gugas749.abyssitems.items.weapons.marshknife.MarshmelloKnifeItem;
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
     *** ITEMS
     */
    public static final DeferredHolder<Item, Item> COMPLETE_KEY = ITEMS.register
            ("complete_key", CompleteKeyItem::new);
    public static final DeferredHolder<Item, Item> TOP_PART_KEY = ITEMS.register
            ("top_part_key", TopPartKeyItem::new);
    public static final DeferredHolder<Item, Item> MIDDLE_PART_KEY = ITEMS.register
            ("middle_part_key", MiddlePartKeyItem::new);
    public static final DeferredHolder<Item, Item> BOTTOM_PART_KEY = ITEMS.register
            ("bottom_part_key", BottomPartKeyItem::new);




    /*
     *** REGISTER
     */

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
