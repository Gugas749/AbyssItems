package com.gugas749.abyssitems.items.normalitems.keys;

import com.gugas749.abyssitems.items.utils.AbyssRarities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MiddlePartKeyItem extends Item {
    public MiddlePartKeyItem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(AbyssRarities.VOID_RARITY.getValue())
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        tooltipComponents.add(Component.translatable("item.abyssitems.middle_part_key.tooltip"));
    }
}
