package com.gugas749.abyssitems.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.gugas749.abyssitems.Abyssitems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockedItemsConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("abyssitems_itemguard.json");

    // Thread-safe set of blocked item resource locations (e.g. "minecraft:diamond")
    private static volatile Set<ResourceLocation> blockedItems = new HashSet<>();

    /**
     * Load (or reload) the blocked items list from the JSON config file.
     * Creates a default config if none exists.
     */
    public static synchronized void load() {
        if (!Files.exists(CONFIG_PATH)) {
            createDefaultConfig();
        }

        try (Reader reader = new InputStreamReader(
                new FileInputStream(CONFIG_PATH.toFile()), StandardCharsets.UTF_8)) {

            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            Set<ResourceLocation> loaded = new HashSet<>();

            if (root != null && root.has("blocked_items")) {
                JsonArray arr = root.getAsJsonArray("blocked_items");
                arr.forEach(el -> {
                    String raw = el.getAsString().trim();
                    // Support optional "item:" prefix for convenience
                    if (raw.startsWith("item:")) {
                        raw = raw.substring(5);
                    }
                    try {
                        ResourceLocation rl = ResourceLocation.parse(raw);
                        loaded.add(rl);
                    } catch (Exception e) {
                        Abyssitems.LOGGER.warn("AbyssItems: Skipping invalid item ID '{}': {}", raw, e.getMessage());
                    }
                });
            }

            blockedItems = Collections.unmodifiableSet(loaded);
            Abyssitems.LOGGER.info("AbyssItems: Loaded {} blocked item(s) from config.", loaded.size());

        } catch (Exception e) {
            Abyssitems.LOGGER.error("AbyssItems: Failed to load config from {}: {}", CONFIG_PATH, e.getMessage());
        }
    }

    /**
     * Returns true if the given item ResourceLocation is in the blocked list.
     */
    public static boolean isBlocked(ResourceLocation itemId) {
        return blockedItems.contains(itemId);
    }

    public static Set<ResourceLocation> getBlockedItems() {
        return blockedItems;
    }

    private static void createDefaultConfig() {
        try {
            JsonObject root = new JsonObject();
            JsonArray arr = new JsonArray();
            // Example entries — empty by default, commented via description field
            root.addProperty("_comment", "Add item IDs to block non-op players from holding. Format: 'modid:itemname'");
            root.add("blocked_items", arr);

            try (Writer w = new OutputStreamWriter(
                    new FileOutputStream(CONFIG_PATH.toFile()), StandardCharsets.UTF_8)) {
                GSON.toJson(root, w);
            }

            Abyssitems.LOGGER.info("AbyssItems: Created default config at {}", CONFIG_PATH);
        } catch (Exception e) {
            Abyssitems.LOGGER.error("AbyssItems: Could not create default config: {}", e.getMessage());
        }
    }
}
