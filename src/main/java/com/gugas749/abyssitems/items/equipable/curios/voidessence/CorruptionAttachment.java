package com.gugas749.abyssitems.items.equipable.curios.voidessence;

/**
 * Simple data container stored per-player via NeoForge AttachmentType.
 * Holds the corruption value (0-100) and a tick counter for rate control.
 */
public class CorruptionAttachment {

    public static final int MAX_CORRUPTION = 100;
    // 5 real days = 8,640,000 ticks total → 1 point per 86,400 ticks
    public static final int TICKS_PER_POINT = 86_400;

    private int corruption = 0;
    private int tickCounter = 0;

    public int getCorruption() { return corruption; }

    public void setCorruption(int value) {
        this.corruption = Math.max(0, Math.min(MAX_CORRUPTION, value));
    }

    public int getTickCounter() { return tickCounter; }

    public void incrementTick() { tickCounter++; }

    public void resetTickCounter() { tickCounter = 0; }
}
