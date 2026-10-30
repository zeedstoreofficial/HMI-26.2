package com.holdmylua.source.scripting.custom_api;

import com.holdmylua.source.annotation.Safe;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public class KeyBindManager {
    @Safe
    public boolean isKeyPressed(int keyCode) {
        if (keyCode <= 0) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getWindow() == null) {
            return false;
        }
        return InputConstants.isKeyDown(mc.getWindow(), keyCode);
    }
}
