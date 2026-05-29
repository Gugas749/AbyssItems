package com.gugas749.abyssitems.commands;

import com.gugas749.abyssitems.items.equipable.curios.voidessence.CorruptionRegistry;
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
            () -> Component.translatable("command.abyssitems.check_corruption", target.getName().getString(), corruption),
            false
        );
        return corruption;
    }

    private static int executeSet(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
        int value = IntegerArgumentType.getInteger(ctx, "value");
        target.getData(CorruptionRegistry.CORRUPTION).setCorruption(value);
        ctx.getSource().sendSuccess(
                () -> Component.translatable("command.abyssitems.set_corruption", target.getName().getString(), value),
            true
        );
        return value;
    }

    private static int executeReset(CommandContext<CommandSourceStack> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");
        target.getData(CorruptionRegistry.CORRUPTION).setCorruption(0);
        target.getData(CorruptionRegistry.CORRUPTION).resetTickCounter();
        ctx.getSource().sendSuccess(
            () -> Component.translatable("command.abyssitems.reset_corruption", target.getName().getString()),
            true
        );
        return 1;
    }
}
