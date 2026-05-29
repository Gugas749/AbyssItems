package com.gugas749.abyssitems.items.equipable.curios.voidessence;

import com.gugas749.abyssitems.Abyssitems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class CorruptionRegistry {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Abyssitems.MODID);

    // Codec so the data survives death and relog (serialized to NBT)
    private static final Codec<CorruptionAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("corruption").forGetter(CorruptionAttachment::getCorruption),
            Codec.INT.fieldOf("tick_counter").forGetter(CorruptionAttachment::getTickCounter)
    ).apply(inst, (corruption, ticks) -> {
        CorruptionAttachment a = new CorruptionAttachment();
        a.setCorruption(corruption);
        // tick counter doesn't need to persist across sessions — reset is fine
        return a;
    }));

    public static final Supplier<AttachmentType<CorruptionAttachment>> CORRUPTION =
            ATTACHMENT_TYPES.register("corruption", () ->
                    AttachmentType.builder(CorruptionAttachment::new)
                            .serialize(CODEC)
                            .build()
            );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
