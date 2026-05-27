package com.gugas749.abyssitems.events;

import com.gugas749.abyssitems.config.BlockedItemsConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class InventoryEventHandler {

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.hasPermissions(2)) return;

        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            ResourceLocation itemId = getItemId(stack.getItem());
            if (BlockedItemsConfig.isBlocked(itemId)) {
                inventory.setItem(i, ItemStack.EMPTY);
                sendWarning(player, itemId);
            }
        }

        ItemStack cursor = player.containerMenu.getCarried();
        if (!cursor.isEmpty()) {
            ResourceLocation itemId = getItemId(cursor.getItem());
            if (BlockedItemsConfig.isBlocked(itemId)) {
                player.containerMenu.setCarried(ItemStack.EMPTY);
                sendWarning(player, itemId);
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent.Pre event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (player.hasPermissions(2)) return;

        ItemEntity itemEntity = event.getItemEntity();
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;

        ResourceLocation itemId = getItemId(stack.getItem());
        if (BlockedItemsConfig.isBlocked(itemId)) {
            event.setCanPickup(TriState.FALSE);
            itemEntity.discard();
            sendWarning(player, itemId);
        }
    }

    @SubscribeEvent
    public void onItemEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;

        if (BlockedItemsConfig.isBlocked(getItemId(stack.getItem()))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.hasPermissions(2)) return;

        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;
            ResourceLocation itemId = getItemId(stack.getItem());
            if (BlockedItemsConfig.isBlocked(itemId)) {
                inventory.setItem(i, ItemStack.EMPTY);
                sendWarning(player, itemId);
            }
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static ResourceLocation getItemId(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    private static void sendWarning(ServerPlayer player, ResourceLocation itemId) {
        player.displayClientMessage(
            Component.translatable("itemGuard.abyssitems.itemRemoved", itemId.toString()),
            false
        );
    }
}
