package com.alessandro.astages.ftbquests.api.util;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.overlay.EditStageRewardOverlay;
import com.mojang.datafixers.util.Function3;
import dev.ftb.mods.ftblibrary.config.BooleanConfig;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;

@NotNullParams
public class AFTBQuestsUtils {
    public static void setGuiProvider(RewardType type, Function3<Quest, StringConfig, BooleanConfig, Reward> reward) {
        type.setGuiProvider((panel, quest, callback) -> {
            var stageConfig = new StringConfig();

            var removeConfig = new BooleanConfig();
            removeConfig.setValue(false);

            EditStageRewardOverlay overlay = new EditStageRewardOverlay(panel.getGui(), stageConfig, removeConfig, accepted -> {
                if (accepted) {
                    callback.accept(reward.apply(quest, stageConfig, removeConfig));
                }

                panel.run();
            }, type.getDisplayName());

            int centerX = (panel.getGui().width - overlay.width) / 2;
            int centerY = (panel.getGui().height - overlay.height) / 2;

            overlay.setPosAndSize(centerX, centerY, overlay.width, overlay.height);

            panel.getGui().pushModalPanel(overlay);
        });
    }
}
