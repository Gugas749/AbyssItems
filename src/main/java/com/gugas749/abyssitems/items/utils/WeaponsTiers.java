package com.gugas749.abyssitems.items.utils;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class WeaponsTiers implements Tier {

    public static final WeaponsTiers MARSHMELLO = new WeaponsTiers(
            8064,
            125F,
            3F,
            10,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            () -> Ingredient.EMPTY
    );

    private final int uses;
    private final float damage;
    private final float speed;
    private final int enchantmentValue;
    private final TagKey<Block> incorrectBlocksForDrops;
    Supplier<Ingredient> repairIngredient;

    private WeaponsTiers(int uses, float damage, float speed, int enchantmentValue, TagKey<Block> incorrectBlocksForDrops, Supplier<Ingredient> repairIngredient) {
        this.uses = uses;
        this.damage = damage;
        this.speed = speed;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
