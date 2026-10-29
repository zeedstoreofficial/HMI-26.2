package com.holdmylua.source.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.holdmylua.source.annotation.Safe;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class HmiConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("holdmyitems.json");
    private static HmiConfig INSTANCE;

    public boolean enabled = true;
    // Legacy global offsets (kept for Lua backwards compatibility)
    public double xOffset = 0.0;
    public double yOffset = 0.0;
    public double zOffset = 0.0;

    // Main Hand 6-axis
    public double mainItemX = 0.0;
    public double mainItemY = 0.0;
    public double mainItemZ = 0.0;
    public double mainItemPitch = 0.0; // degrees (-180 to 180)
    public double mainItemYaw = 0.0;
    public double mainItemRoll = 0.0;
    public double mainItemScale = 1.0; // 0.1 to 3.0 (default 1.0)
    // Off Hand 6-axis
    public double offItemX = 0.0;
    public double offItemY = 0.0;
    public double offItemZ = 0.0;
    public double offItemPitch = 0.0;
    public double offItemYaw = 0.0;
    public double offItemRoll = 0.0;
    public double offItemScale = 1.0; // 0.1 to 3.0 (default 1.0)
    public String inspectKeyName = "J";
    public int inspectKeyCode = 74; // GLFW legacy code (J)
    public int inspectScancode = 13; // SDL scancode (KEY_J)

    public static HmiConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    @Safe
    public boolean isEnabled() {
        return this.enabled;
    }
    @Safe
    public double getXOffset() {
        return this.xOffset;
    }

    @Safe
    public double getMainItemX() { return this.mainItemX; }
    @Safe
    public double getMainItemY() { return this.mainItemY; }
    @Safe
    public double getMainItemZ() { return this.mainItemZ; }
    @Safe
    public double getMainItemPitch() { return this.mainItemPitch; }
    @Safe
    public double getMainItemYaw() { return this.mainItemYaw; }
    @Safe
    public double getMainItemRoll() { return this.mainItemRoll; }
    @Safe
    public double getMainItemScale() { return this.mainItemScale; }
    @Safe
    public double getOffItemX() { return this.offItemX; }
    @Safe
    public double getOffItemY() { return this.offItemY; }
    @Safe
    public double getOffItemZ() { return this.offItemZ; }
    @Safe
    public double getOffItemPitch() { return this.offItemPitch; }
    @Safe
    public double getOffItemYaw() { return this.offItemYaw; }
    @Safe
    public double getOffItemRoll() { return this.offItemRoll; }
    @Safe
    public double getOffItemScale() { return this.offItemScale; }

    @Safe
    public double getYOffset() {
        return this.yOffset;
    }

    @Safe
    public double getZOffset() {
        return this.zOffset;
    }

    @Safe
    public int getInspectKey() {
        return this.inspectKeyCode;
    }

    public static HmiConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                HmiConfig config = GSON.fromJson(reader, HmiConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (Exception e) {
                System.err.println("[HoldMyItems] Failed to load config, using defaults: " + e.getMessage());
            }
        }
        HmiConfig config = new HmiConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (Exception e) {
            System.err.println("[HoldMyItems] Failed to save config: " + e.getMessage());
        }
    }

    public void reset() {
        this.enabled = true;
        this.xOffset = 0.0;
        this.yOffset = 0.0;
        this.zOffset = 0.0;
        this.mainItemX = 0.0;
        this.mainItemY = 0.0;
        this.mainItemZ = 0.0;
        this.mainItemPitch = 0.0;
        this.mainItemYaw = 0.0;
        this.mainItemRoll = 0.0;
        this.mainItemScale = 1.0;
        this.offItemX = 0.0;
        this.offItemY = 0.0;
        this.offItemZ = 0.0;
        this.offItemPitch = 0.0;
        this.offItemYaw = 0.0;
        this.offItemRoll = 0.0;
        this.offItemScale = 1.0;
        this.inspectKeyName = "J";
        this.inspectKeyCode = 74;
        this.inspectScancode = 13;
    }
}
