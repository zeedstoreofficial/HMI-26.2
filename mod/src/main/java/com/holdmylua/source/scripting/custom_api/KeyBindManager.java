package com.holdmylua.source.scripting.custom_api;

import com.holdmylua.source.annotation.Safe;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeyBindManager {
    @Safe
    public boolean isKeyPressed(int keyCode) {
        int scancode = legacyKeyToScancode(keyCode);
        Minecraft mc = Minecraft.getInstance();
        return scancode != 0 && mc != null && mc.getWindow() != null && InputConstants.isKeyDown(mc.getWindow(), scancode);
    }

    // Lua and saved resource-pack options retain GLFW's physical-key tokens.
    static int legacyKeyToScancode(int keyCode) {
        if (keyCode >= 65 && keyCode <= 90) return keyCode - 65 + 4;
        if (keyCode >= 49 && keyCode <= 57) return keyCode - 49 + 30;
        if (keyCode >= 290 && keyCode <= 301) return keyCode - 290 + 58;
        if (keyCode >= 302 && keyCode <= 313) return keyCode - 302 + 104;
        if (keyCode >= 321 && keyCode <= 329) return keyCode - 321 + 89;
        return switch (keyCode) {
            case 32 -> 44;
            case 39 -> 52;
            case 44 -> 54;
            case 45 -> 45;
            case 46 -> 55;
            case 47 -> 56;
            case 48 -> 39;
            case 59 -> 51;
            case 61 -> 46;
            case 91 -> 47;
            case 92 -> 49;
            case 93 -> 48;
            case 96 -> 53;
            case 161 -> 100;
            case 162 -> 50;
            case 256 -> 41;
            case 257 -> 40;
            case 258 -> 43;
            case 259 -> 42;
            case 260 -> 73;
            case 261 -> 76;
            case 262 -> 79;
            case 263 -> 80;
            case 264 -> 81;
            case 265 -> 82;
            case 266 -> 75;
            case 267 -> 78;
            case 268 -> 74;
            case 269 -> 77;
            case 280 -> 57;
            case 281 -> 71;
            case 282 -> 83;
            case 283 -> 70;
            case 284 -> 72;
            case 320 -> 98;
            case 330 -> 99;
            case 331 -> 84;
            case 332 -> 85;
            case 333 -> 86;
            case 334 -> 87;
            case 335 -> 88;
            case 336 -> 103;
            case 340 -> 225;
            case 341 -> 224;
            case 342 -> 226;
            case 343 -> 227;
            case 344 -> 229;
            case 345 -> 228;
            case 346 -> 230;
            case 347 -> 231;
            case 348 -> 101;
            // SDL has no F25 token; never poll a different key for unmapped input.
            default -> 0;
        };
    }

    public static int scancodeToLegacyKey(int scancode) {
        if (scancode >= 4 && scancode <= 29) return scancode - 4 + 65;
        if (scancode >= 30 && scancode <= 38) return scancode - 30 + 49;
        if (scancode >= 58 && scancode <= 69) return scancode - 58 + 290;
        if (scancode >= 104 && scancode <= 115) return scancode - 104 + 302;
        if (scancode >= 89 && scancode <= 97) return scancode - 89 + 321;
        return switch (scancode) {
            case 44 -> 32;
            case 52 -> 39;
            case 54 -> 44;
            case 45 -> 45;
            case 55 -> 46;
            case 56 -> 47;
            case 39 -> 48;
            case 51 -> 59;
            case 46 -> 61;
            case 47 -> 91;
            case 49 -> 92;
            case 48 -> 93;
            case 53 -> 96;
            case 100 -> 161;
            case 50 -> 162;
            case 41 -> 256;
            case 40 -> 257;
            case 43 -> 258;
            case 42 -> 259;
            case 73 -> 260;
            case 76 -> 261;
            case 79 -> 262;
            case 80 -> 263;
            case 81 -> 264;
            case 82 -> 265;
            case 75 -> 266;
            case 78 -> 267;
            case 74 -> 268;
            case 77 -> 269;
            case 57 -> 280;
            case 71 -> 281;
            case 83 -> 282;
            case 70 -> 283;
            case 72 -> 284;
            case 98 -> 320;
            case 99 -> 330;
            case 84 -> 331;
            case 85 -> 332;
            case 86 -> 333;
            case 87 -> 334;
            case 88 -> 335;
            case 103 -> 336;
            case 225 -> 340;
            case 224 -> 341;
            case 226 -> 342;
            case 227 -> 343;
            case 229 -> 344;
            case 228 -> 345;
            case 230 -> 346;
            case 231 -> 347;
            case 101 -> 348;
            default -> 0;
        };
    }
}

