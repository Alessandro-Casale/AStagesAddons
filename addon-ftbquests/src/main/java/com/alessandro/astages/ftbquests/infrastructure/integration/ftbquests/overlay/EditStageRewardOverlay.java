package com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.overlay;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import dev.ftb.mods.ftblibrary.config.BooleanConfig;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class EditStageRewardOverlay extends ModalPanel {
    private static final Icon DEFAULT_ICON = Icons.CONTROLLER;

    private final BooleanConfig removeConfig;
    private final Component title;

    private final SimpleTextButton removeButton;
    private final SimpleTextButton acceptButton;
    private final SimpleTextButton cancelButton;

    private final TextBox stageField;
    private final Widget stageIconWidget;
    private Icon currentStageIcon;

    public EditStageRewardOverlay(BaseScreen gui, StringConfig stageConfig, BooleanConfig removeConfig, Consumer<Boolean> callback, Component title) {
        super(gui);
        this.removeConfig = removeConfig;
        this.title = title;
        this.currentStageIcon = DEFAULT_ICON;

        setSize(180, 110);

        this.stageIconWidget = new Widget(this) {
            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                currentStageIcon.draw(graphics, x, y, w, h);
            }
        };

        updateStageIcon(stageConfig.getValue());

        this.stageField = new TextBox(this) {
            @Override
            public void onTextChanged() {
                stageConfig.setValue(getText());
                updateStageIcon(getText());
            }

            @Override
            public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                super.draw(graphics, theme, x, y, w, h);

                if (getText().isEmpty() && !isFocused()) {
                    graphics.pose().pushPose();
                    graphics.pose().translate(0, 0, 5);

                    int textY = y + (h - theme.getFontHeight()) / 2 + 1;
                    graphics.drawString(theme.getFont(), Component.translatable("ftbquests.overlay.astages.placeholder"), x + 4, textY, 0xFFAAAAAA, false);

                    graphics.pose().popPose();
                }
            }
        };
        this.stageField.setText(stageConfig.getValue() != null ? stageConfig.getValue() : "");

        this.removeButton = new SimpleTextButton(this, getActionComponent(), Icons.REFRESH) {
            @Override
            public void onClicked(MouseButton button) {
                removeConfig.setValue(!removeConfig.getValue());
                setTitle(getActionComponent());

                playClickSound();
                EditStageRewardOverlay.this.alignWidgets();
            }
        };

        this.acceptButton = new SimpleTextButton(this, Component.translatable("gui.accept"), Icons.ACCEPT) {
            @Override
            public void onClicked(MouseButton button) {
                getGui().popModalPanel();
                callback.accept(true);
            }
        };

        this.cancelButton = new SimpleTextButton(this, Component.translatable("gui.cancel"), Icons.CANCEL) {
            @Override
            public void onClicked(MouseButton button) {
                getGui().popModalPanel();
                callback.accept(false);
            }
        };
    }

    private void updateStageIcon(@Nullable String stageKey) {
        if (stageKey == null || stageKey.isEmpty()) {
            currentStageIcon = DEFAULT_ICON;
            return;
        }

        var restriction = AClientStageManager.GENERIC_INSTANCE.getStage(stageKey);
        if (restriction != null && !restriction.isValueNull(StageAttributes.ICON)) {
            var stack = restriction.get(StageAttributes.ICON);
            currentStageIcon = ItemIcon.getItemIcon(stack);
        } else {
            currentStageIcon = DEFAULT_ICON;
        }
    }

    private Component getActionComponent() {
        boolean isRemove = removeConfig.getValue();
        return isRemove ? Component.translatable("ftbquests.overlay.astages.action.remove") : Component.translatable("ftbquests.overlay.astages.action.add");
    }

    @Override
    public void addWidgets() {
        add(stageField);
        add(stageIconWidget);
        add(removeButton);
        add(acceptButton);
        add(cancelButton);

        stageField.setFocused(true);
    }

    @Override
    public void alignWidgets() {
        int iconSize = 16;
        int spacing = 6;

        stageField.setPosAndSize(10, 34, width - 20 - iconSize - spacing, 16);
        stageIconWidget.setPosAndSize(width - 10 - iconSize, 34, iconSize, iconSize);

        removeButton.setPosAndSize(10, 58, width - 20, 16);

        int btnWidth = (width - 25) / 2;
        acceptButton.setPosAndSize(10, 84, btnWidth, 16);
        cancelButton.setPosAndSize(15 + btnWidth, 84, btnWidth, 16);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 600);
        super.draw(graphics, theme, x, y, w, h);
        graphics.pose().popPose();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawGui(graphics, x, y, w, h, WidgetType.NORMAL);

        // Borders
        int borderColor = 0xFF202020;
        graphics.fill(x, y - 1, x + w, y, borderColor); // Top
        graphics.fill(x, y + h, x + w, y + h + 1, borderColor); // Bottom
        graphics.fill(x - 1, y, x, y + h, borderColor); // Left
        graphics.fill(x + w, y, x + w + 1, y + h, borderColor); // Right

        theme.drawString(graphics, title, x + 10, y + 6, Theme.SHADOW);
        theme.drawString(graphics, Component.translatable("ftbquests.reward.astages.stage_description"), x + 10, y + 22, Theme.SHADOW);
    }

    @Override
    public boolean keyPressed(Key key) {
        if (key.enter()) {
            acceptButton.onClicked(MouseButton.LEFT);
            return true;
        } else if (key.esc()) {
            cancelButton.onClicked(MouseButton.LEFT);
            return true;
        }

        return super.keyPressed(key);
    }
}