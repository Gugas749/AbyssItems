package com.gugas749.abyssitems.commands;

import com.gugas749.abyssitems.config.BlockedItemsConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class ItemGuardCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("itemguard")
                .requires(source -> source.hasPermission(2))

                .then(Commands.literal("reload")
                    .executes(ItemGuardCommand::executeReload))

                .then(Commands.literal("list")
                    .executes(ItemGuardCommand::executeList))

                .then(Commands.literal("check")
                    .then(Commands.argument("item_id", StringArgumentType.word())
                        .executes(ItemGuardCommand::executeCheck)))

                .then(Commands.literal("scan")
                    .executes(ItemGuardCommand::executeScan))
        );
    }

    private static int executeReload(CommandContext<CommandSourceStack> ctx) {
        BlockedItemsConfig.load();
        int count = BlockedItemsConfig.getBlockedItems().size();
        ctx.getSource().sendSuccess(
            () -> Component.translatable("itemGuard.abyssitems.reloadSuccess", count),
            true
        );
        return 1;
    }

    private static int executeList(CommandContext<CommandSourceStack> ctx) {
        Set<ResourceLocation> blocked = BlockedItemsConfig.getBlockedItems();
        if (blocked.isEmpty()) {
            ctx.getSource().sendSuccess(
                () -> Component.translatable("itemGuard.abyssitems.listEmpty"),
                false
            );
        } else {
            ctx.getSource().sendSuccess(
                () -> Component.translatable("itemGuard.abyssitems.listHeader", blocked.size()),
                false
            );
            for (ResourceLocation rl : blocked) {
                ctx.getSource().sendSuccess(
                    () -> Component.translatable("itemGuard.abyssitems.listEntry", rl),
                    false
                );
            }
        }
        return blocked.size();
    }

    private static int executeCheck(CommandContext<CommandSourceStack> ctx) {
        String raw = StringArgumentType.getString(ctx, "item_id");
        try {
            ResourceLocation rl = ResourceLocation.parse(raw);
            boolean isBlocked = BlockedItemsConfig.isBlocked(rl);
            ctx.getSource().sendSuccess(
                () -> Component.translatable(
                    isBlocked
                        ? "itemGuard.abyssitems.checkBlocked"
                        : "itemGuard.abyssitems.checkNotBlocked",
                    rl
                ),
                false
            );
        } catch (Exception e) {
            ctx.getSource().sendFailure(
                Component.translatable("itemGuard.abyssitems.checkInvalidId", raw)
            );
        }
        return 1;
    }

    private static int executeScan(CommandContext<CommandSourceStack> ctx) {
        var server = ctx.getSource().getServer();
        if (server == null) return 0;

        int removedTotal = 0;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.hasPermissions(2)) continue;

            var inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                var stack = inventory.getItem(i);
                if (stack.isEmpty()) continue;
                var itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
                if (BlockedItemsConfig.isBlocked(itemId)) {
                    inventory.setItem(i, ItemStack.EMPTY);
                    player.sendSystemMessage(
                        Component.translatable("itemGuard.abyssitems.itemRemoved", itemId)
                    );
                    removedTotal++;
                }
            }
        }

        final int finalCount = removedTotal;
        ctx.getSource().sendSuccess(
            () -> Component.translatable("itemGuard.abyssitems.scanFinished", finalCount),
            true
        );
        return 1;
    }
}
