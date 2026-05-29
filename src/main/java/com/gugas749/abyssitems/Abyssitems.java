package com.gugas749.abyssitems;

import com.gugas749.abyssitems.commands.ItemGuardCommand;
import com.gugas749.abyssitems.config.BlockedItemsConfig;
import com.gugas749.abyssitems.events.InventoryEventHandler;
import com.gugas749.abyssitems.commands.CorruptionCommand;
import com.gugas749.abyssitems.items.equipable.curios.voidessence.CorruptionEventHandler;
import com.gugas749.abyssitems.items.equipable.curios.voidessence.CorruptionRegistry;
import com.gugas749.abyssitems.items.registrie.AbyssCreativeModeTabs;
import com.gugas749.abyssitems.items.registrie.ItemsRegistry;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(Abyssitems.MODID)
public class Abyssitems {
    public static final String MODID = "abyssitems";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Abyssitems(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        //--------
        NeoForge.EVENT_BUS.register(new InventoryEventHandler());
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
        NeoForge.EVENT_BUS.register(new CorruptionEventHandler());
        //--------
        ItemsRegistry.register(modEventBus);
        CorruptionRegistry.register(modEventBus);
        //--------
        AbyssCreativeModeTabs.register(modEventBus);
        //--------
        BlockedItemsConfig.load();
        //--------
        LOGGER.info("[AbyssItems] initialized.");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[AbyssItems] common setup complete.");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        ItemGuardCommand.register(event.getDispatcher());
        CorruptionCommand.register(event.getDispatcher());
    }
}
