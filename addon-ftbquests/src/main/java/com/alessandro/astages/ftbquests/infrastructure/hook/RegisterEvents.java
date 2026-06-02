package com.alessandro.astages.ftbquests.infrastructure.hook;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import com.alessandro.astages.ftbquests.api.util.AFTBQuestsUtils;
import com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.reward.APlayerReward;
import com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.reward.AServerReward;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = AStagesFTBQuests.MODID)
public class RegisterEvents {
    @SubscribeEvent
    public static void registerReward(RegisterEvent event) {
        AStagesFTBQuests.PLAYER_REWARD_TYPE = RewardTypes.register(AResourceLocation.fromNamespaceAndPath("player"), APlayerReward::new, () -> Icons.CONTROLLER);
        AStagesFTBQuests.SERVER_REWARD_TYPE = RewardTypes.register(AResourceLocation.fromNamespaceAndPath("server"), AServerReward::new, () -> Icons.CONTROLLER);

        AFTBQuestsUtils.setGuiProvider(
            AStagesFTBQuests.PLAYER_REWARD_TYPE,
            (quest, stageConfig, removeConfig) -> new APlayerReward(0, quest, stageConfig.getValue(), removeConfig.getValue())
        );

        AFTBQuestsUtils.setGuiProvider(
            AStagesFTBQuests.SERVER_REWARD_TYPE,
            (quest, stageConfig, removeConfig) -> new AServerReward(0, quest, stageConfig.getValue(), removeConfig.getValue())
        );
    }
}