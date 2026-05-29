package com.gugas749.abyssitems.items.equipable.curios.voidessence;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.gugas749.abyssitems.items.utils.AbyssRarities;
import net.acetheeldritchking.aces_spell_utils.registries.ASAttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class VoidEssenceItem extends Item implements ICurioItem {

    private static final double ATTACK_DAMAGE_BONUS = 0.25;
    private static final double EVASION_BONUS = 0.25;

    public VoidEssenceItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .fireResistant()
                .rarity(AbyssRarities.VOID_RARITY.getValue())
        );
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> attr = LinkedHashMultimap.create();
        attr.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(id, ATTACK_DAMAGE_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attr.put(ASAttributeRegistry.EVASIVE, new AttributeModifier(id, EVASION_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return attr;
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips, Item.TooltipContext context, ItemStack stack) {
        tooltips.add(attributeTooltip(Attributes.ATTACK_DAMAGE));
        tooltips.add(Component.translatable("attribute.modifier.plus.1",
                (int)(EVASION_BONUS * 100) + "",
                Component.translatable("item.abyssitems.void_essence.attribute.evasive")
        ).withStyle(ChatFormatting.YELLOW));
        return tooltips;
    }

    private static MutableComponent attributeTooltip(Holder<Attribute> attribute) {
        return Component.translatable(
                "attribute.modifier.plus.1",
                25,
                Component.translatable(attribute.value().getDescriptionId())
        ).withStyle(ChatFormatting.YELLOW);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player )
        {

        }
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("item.abyssitems.void_essence.tooltip1")
                .withStyle(ChatFormatting.DARK_PURPLE));

        tips.add(Component.translatable("item.abyssitems.void_essence.tooltip2")
                .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.abyssitems.void_essence");
    }
}
