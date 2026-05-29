package com.gugas749.abyssitems.items.equipable.curios.voidessence;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CorruptionCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("corruption")
                .requires(source -> source.hasPermission(2))

                // /corruption check <player>
                .then(Commands.literal("check")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(CorruptionCommand::executeCheck)))

                // /corruption set <player> <value>
                .then(Commands.literal("set")
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("value", IntegerArgumentType.integer(0, 100))
                            .executes(CorruptionCommand::executeSet))))

                // /corruption reset <player>
                .then(Commands.literal("reset")
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(CorruptionCommand::executeReset)))
        );
    }

    private static int executeCheck(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
        int corruption = target.getData(CorruptionRegistry.CORRUPTION).getCorruption();
        ctx.getSource().sendSuccess(
            () -> Component.literal("§5[Corruption] §f" + target.getName().getString()
                    + " §7corruption: §d" + corruption + "%"),
            false
        );
        return corruption;
    }

    private static int executeSet(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
        int value = IntegerArgumentType.getInteger(ctx, "value");
        target.getData(CorruptionRegistry.CORRUPTION).setCorruption(value);
        ctx.getSource().sendSuccess(
            () -> Component.literal("§5[Corruption] §fSet §f" + target.getName().getString()
                    + "§7's corruption to §d" + value + "%"),
            true
        );
        return value;
    }

    private static int executeReset(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
        target.getData(CorruptionRegistry.CORRUPTION).setCorruption(0);
        target.getData(CorruptionRegistry.CORRUPTION).resetTickCounter();
        ctx.getSource().sendSuccess(
            () -> Component.literal("§5[Corruption] §fReset §f" + target.getName().getString()
                    + "§7's corruption to §d0%"),
            true
        );
        return 1;
    }
}
