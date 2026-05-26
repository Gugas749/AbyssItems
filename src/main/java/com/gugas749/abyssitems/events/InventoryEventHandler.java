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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

public class InventoryEventHandler {

    /**
     * Fired every server tick per player — scans the full inventory and removes blocked items.
     */
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Only enforce on non-ops (permission level < 2 = no OP commands)
        if (player.hasPermissions(2)) return;

        // Scan main inventory + armor + offhand
        var inventory = player.getInventory();
        boolean removed = false;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.isEmpty()) continue;

            ResourceLocation itemId = getItemId(stack.getItem());
            if (BlockedItemsConfig.isBlocked(itemId)) {
                inventory.setItem(i, ItemStack.EMPTY);
                removed = true;
                sendWarning(player, itemId);
            }
        }

        // Also check cursor slot (item held by mouse in open container)
        ItemStack cursor = player.containerMenu.getCarried();
        if (!cursor.isEmpty()) {
            ResourceLocation itemId = getItemId(cursor.getItem());
            if (BlockedItemsConfig.isBlocked(itemId)) {
                player.containerMenu.setCarried(ItemStack.EMPTY);
                sendWarning(player, itemId);
            }
        }
    }

    /**
     * Fired when a player tries to pick up an item entity from the ground.
     * Cancels the pickup and destroys the item entity if blocked.
     */
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
            itemEntity.discard(); // Remove the item from the world too
            sendWarning(player, itemId);
        }
    }

    /**
     * Fired when an item entity spawns in the world.
     * Destroys it immediately if it's a blocked item — prevents it from sitting on the ground.
     */
    @SubscribeEvent
    public void onItemEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) return;

        ResourceLocation itemId = getItemId(stack.getItem());
        if (BlockedItemsConfig.isBlocked(itemId)) {
            event.setCanceled(true); // Prevent the item entity from joining the world
        }
    }

    /**
     * Fired when a player logs in — do an immediate inventory scan.
     */
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
        player.sendSystemMessage(
            Component.translatable("itemGuard.abyssitems.itemRemoved", itemId)
        );
    }
}
