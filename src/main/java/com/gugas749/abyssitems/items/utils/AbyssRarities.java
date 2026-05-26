package com.gugas749.abyssitems.items.utils;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.function.UnaryOperator;

public class AbyssRarities {

    public static final EnumProxy<Rarity> MARSHMELLO_RARITY = new EnumProxy<>(Rarity.class,
            -1,
            "abyssitems:marshmello",
            (UnaryOperator<Style>) ((style) -> style.withColor(0x00FFFF))
    );
}
