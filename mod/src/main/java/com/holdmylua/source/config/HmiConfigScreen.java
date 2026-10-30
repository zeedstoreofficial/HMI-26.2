package com.holdmylua.source.config;

import com.holdmylua.source.scripting.custom_api.KeyBindManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class HmiConfigScreen extends Screen {
    private final Screen parent;
    private final HmiConfig config;

    private int currentTab = 0;
    private Button keybindButton;
    private boolean isListeningForKey = false;

    public HmiConfigScreen(Screen parent) {
        super(Component.literal("Hold My Items Settings"));
        this.parent = parent;
        this.config = HmiConfig.getInstance();
    }

    @Override
    protected void init() {
        this.clearWidgets();

        int panelWidth = 320;
        int rowHeight = 20;
        int resetBtnWidth = 50;
        int sliderWidth = panelWidth - resetBtnWidth - 4;
        int tabSpacing = 4;
        int tabWidth = (panelWidth - tabSpacing * 2) / 3;
        int spacing = 23;

        int startX = (this.width - panelWidth) / 2;
        int startY = 32;

        String[] tabTitles = {"Main Hand", "Off Hand", "General"};
        for (int i = 0; i < 3; i++) {
            final int t = i;
            boolean isActive = (this.currentTab == t);
            int currentTabWidth = (i == 2) ? (panelWidth - (tabWidth + tabSpacing) * 2) : tabWidth;
            Button tabBtn = Button.builder(Component.literal(tabTitles[i]), b -> {
                this.currentTab = t;
                this.init();
            }).bounds(startX + i * (tabWidth + tabSpacing), startY, currentTabWidth, rowHeight).build();
            tabBtn.active = !isActive;
            this.addRenderableWidget(tabBtn);
        }

        int contentY = startY + spacing + 6;

        if (this.currentTab == 0 || this.currentTab == 1) {
            boolean isMain = (this.currentTab == 0);
            String handPrefix = isMain ? "Main Hand" : "Off Hand";

            addBipolarSlider(handPrefix + " X", startX, contentY + spacing * 0, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemX : config.offItemX,
                    val -> { if (isMain) config.mainItemX = val; else config.offItemX = val; config.save(); },
                    1.0, 0.0);

            addBipolarSlider(handPrefix + " Y", startX, contentY + spacing * 1, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemY : config.offItemY,
                    val -> { if (isMain) config.mainItemY = val; else config.offItemY = val; config.save(); },
                    1.0, 0.0);

            addBipolarSlider(handPrefix + " Z", startX, contentY + spacing * 2, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemZ : config.offItemZ,
                    val -> { if (isMain) config.mainItemZ = val; else config.offItemZ = val; config.save(); },
                    1.0, 0.0);

            addBipolarSlider(handPrefix + " Pitch", startX, contentY + spacing * 3, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemPitch : config.offItemPitch,
                    val -> { if (isMain) config.mainItemPitch = val; else config.offItemPitch = val; config.save(); },
                    180.0, 0.0);

            addBipolarSlider(handPrefix + " Yaw", startX, contentY + spacing * 4, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemYaw : config.offItemYaw,
                    val -> { if (isMain) config.mainItemYaw = val; else config.offItemYaw = val; config.save(); },
                    180.0, 0.0);

            addBipolarSlider(handPrefix + " Roll", startX, contentY + spacing * 5, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemRoll : config.offItemRoll,
                    val -> { if (isMain) config.mainItemRoll = val; else config.offItemRoll = val; config.save(); },
                    180.0, 0.0);

            addScaleSlider(handPrefix + " Scale", startX, contentY + spacing * 6, sliderWidth, rowHeight, resetBtnWidth,
                    () -> isMain ? config.mainItemScale : config.offItemScale,
                    val -> { if (isMain) config.mainItemScale = val; else config.offItemScale = val; config.save(); });

            this.addRenderableWidget(Button.builder(Component.literal("Reset All " + handPrefix), b -> {
                if (isMain) {
                    config.mainItemX = 0.0;
                    config.mainItemY = 0.0;
                    config.mainItemZ = 0.0;
                    config.mainItemPitch = 0.0;
                    config.mainItemYaw = 0.0;
                    config.mainItemRoll = 0.0;
                    config.mainItemScale = 1.0;
                } else {
                    config.offItemX = 0.0;
                    config.offItemY = 0.0;
                    config.offItemZ = 0.0;
                    config.offItemPitch = 0.0;
                    config.offItemYaw = 0.0;
                    config.offItemRoll = 0.0;
                    config.offItemScale = 1.0;
                }
                config.save();
                this.init();
            }).bounds(startX, contentY + spacing * 7 + 4, panelWidth, rowHeight).build());

        } else {
            this.addRenderableWidget(Button.builder(
                    Component.literal("Hold My Items: " + (config.enabled ? "Enabled" : "Disabled")),
                    b -> {
                        config.enabled = !config.enabled;
                        config.save();
                        b.setMessage(Component.literal("Hold My Items: " + (config.enabled ? "Enabled" : "Disabled")));
                    }
            ).bounds(startX, contentY, panelWidth, rowHeight).build());

            addBipolarSlider("Arm Offset X", startX, contentY + spacing * 1, sliderWidth, rowHeight, resetBtnWidth,
                    () -> config.xOffset, val -> { config.xOffset = val; config.save(); }, 1.0, 0.0);

            addBipolarSlider("Arm Offset Y", startX, contentY + spacing * 2, sliderWidth, rowHeight, resetBtnWidth,
                    () -> config.yOffset, val -> { config.yOffset = val; config.save(); }, 1.0, 0.0);

            addBipolarSlider("Arm Offset Z", startX, contentY + spacing * 3, sliderWidth, rowHeight, resetBtnWidth,
                    () -> config.zOffset, val -> { config.zOffset = val; config.save(); }, 1.0, 0.0);

            this.keybindButton = this.addRenderableWidget(Button.builder(
                    getKeybindButtonMessage(),
                    button -> {
                        this.isListeningForKey = true;
                        button.setMessage(Component.literal("Inspect Key: Press any key"));
                    }
            ).bounds(startX, contentY + spacing * 4, panelWidth, rowHeight).build());

            this.addRenderableWidget(Button.builder(Component.literal("Reset All Settings to Defaults"), b -> {
                this.config.reset();
                this.config.save();
                this.init();
            }).bounds(startX, contentY + spacing * 5 + 6, panelWidth, rowHeight).build());
        }

        int doneWidth = 200;
        int doneX = (this.width - doneWidth) / 2;
        int doneY = this.height - 28;
        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
                .bounds(doneX, doneY, doneWidth, rowHeight).build());
    }

    private void addBipolarSlider(String name, int x, int y, int sliderWidth, int rowHeight, int resetBtnWidth,
                                  DoubleSupplier getter, DoubleConsumer setter, double range, double defVal) {
        double current = getter.getAsDouble();
        double initialRatio = Math.max(0.0, Math.min(1.0, ((current / range) + 1.0) / 2.0));

        this.addRenderableWidget(new ConfigSlider(x, y, sliderWidth, rowHeight, initialRatio,
                ratio -> {
                    double val = Math.round(((ratio * 2.0) - 1.0) * range * 100.0) / 100.0;
                    setter.accept(val);
                },
                () -> String.format(Locale.ROOT, "%s: %+.2f", name, getter.getAsDouble())));

        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            setter.accept(defVal);
            this.init();
        }).bounds(x + sliderWidth + 4, y, resetBtnWidth, rowHeight)
          .tooltip(Tooltip.create(Component.literal("Reset to default")))
          .build());
    }

    private void addScaleSlider(String name, int x, int y, int sliderWidth, int rowHeight, int resetBtnWidth,
                                DoubleSupplier getter, DoubleConsumer setter) {
        double minScale = 0.20;
        double maxScale = 2.50;
        double current = getter.getAsDouble();
        double initialRatio = Math.max(0.0, Math.min(1.0, (current - minScale) / (maxScale - minScale)));

        this.addRenderableWidget(new ConfigSlider(x, y, sliderWidth, rowHeight, initialRatio,
                ratio -> {
                    double val = Math.round((minScale + ratio * (maxScale - minScale)) * 100.0) / 100.0;
                    setter.accept(val);
                },
                () -> String.format(Locale.ROOT, "%s: %.2fx", name, getter.getAsDouble())));

        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            setter.accept(1.0);
            this.init();
        }).bounds(x + sliderWidth + 4, y, resetBtnWidth, rowHeight)
          .tooltip(Tooltip.create(Component.literal("Reset to default")))
          .build());
    }

    private Component getKeybindButtonMessage() {
        return Component.literal("Inspect Key: " + config.inspectKeyName);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.isListeningForKey) {
            InputConstants.Key key = InputConstants.getKey(event);
            if (key != InputConstants.UNKNOWN) {
                if (event.isEscape()) {
                    this.isListeningForKey = false;
                    this.keybindButton.setMessage(getKeybindButtonMessage());
                    return true;
                }
                this.config.inspectKeyCode = key.getValue();
                this.config.inspectKeyName = key.getDisplayName().getString();
                this.config.save();
                this.isListeningForKey = false;
                this.keybindButton.setMessage(getKeybindButtonMessage());
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        graphics.centeredText(this.font, "Hold My Items Settings", this.width / 2, 14, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.config.save();
        if (this.minecraft != null) {
            this.minecraft.gui.setScreen(this.parent);
        }
    }

    private static class ConfigSlider extends AbstractSliderButton {
        private final Consumer<Double> onRatioChange;
        private final Supplier<String> messageSupplier;

        public ConfigSlider(int x, int y, int width, int height, double initialValue, Consumer<Double> onRatioChange, Supplier<String> messageSupplier) {
            super(x, y, width, height, Component.literal(messageSupplier.get()), initialValue);
            this.onRatioChange = onRatioChange;
            this.messageSupplier = messageSupplier;
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal(this.messageSupplier.get()));
        }

        @Override
        protected void applyValue() {
            this.onRatioChange.accept(this.value);
        }
    }
}
