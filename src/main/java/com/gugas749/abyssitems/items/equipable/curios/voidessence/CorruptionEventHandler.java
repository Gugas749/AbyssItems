package com.gugas749.abyssitems.items.equipable.curios.voidessence;

import com.gugas749.abyssitems.items.registrie.ItemsRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CorruptionEventHandler {

    // Strength effect is refreshed every 40 ticks (2 seconds) while corrupted
    private static final int EFFECT_REFRESH_TICKS = 40;
    // Duration given to the effect each refresh (slightly more than refresh rate)
    private static final int EFFECT_DURATION = 60;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        boolean wearingItem = isWearingVoidEssence(player);
        CorruptionAttachment data = player.getData(CorruptionRegistry.CORRUPTION);

        // ── Corruption gain ───────────────────────────────────────────────
        if (wearingItem && data.getCorruption() < CorruptionAttachment.MAX_CORRUPTION) {
            data.incrementTick();
            if (data.getTickCounter() >= CorruptionAttachment.TICKS_PER_POINT) {
                data.resetTickCounter();
                data.setCorruption(data.getCorruption() + 1);
            }
        }

        // ── Strength effect based on corruption ───────────────────────────
        // Only apply if player has any corruption and is wearing the item
        if (wearingItem && data.getCorruption() > 0) {
            // Refresh every EFFECT_REFRESH_TICKS to avoid constant effect spam
            if (player.tickCount % EFFECT_REFRESH_TICKS == 0) {
                int strengthLevel = getStrengthLevel(data.getCorruption());
                player.addEffect(new MobEffectInstance(
                        MobEffects.DAMAGE_BOOST,  // Strength
                        EFFECT_DURATION,
                        strengthLevel,
                        false,  // not ambient (no particles reduction)
                        false,   // show particles
                        true    // show icon
                ));
            }
        }
    }

    /**
     * Maps corruption % to strength amplifier (0-indexed):
     *  0-24%   → no strength
     *  25-49%  → Strength I  (amplifier 0)
     *  50-74%  → Strength II (amplifier 1)
     *  75-99%  → Strength III (amplifier 2)
     *  100%    → Strength IV (amplifier 3)
     */
    private static int getStrengthLevel(int corruption) {
        if (corruption >= 100) return 3;
        if (corruption >= 75)  return 2;
        if (corruption >= 50)  return 1;
        return 0; // 25-49%
    }

    private static boolean isWearingVoidEssence(ServerPlayer player) {
        return CuriosApi.getCuriosInventory(player).map(inv -> {
            var allCurios = inv.getCurios();
            for (var entry : allCurios.entrySet()) {
                var stacks = entry.getValue().getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (stacks.getStackInSlot(i).is(ItemsRegistry.VOID_ESSENCE.get())) {
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }
}
