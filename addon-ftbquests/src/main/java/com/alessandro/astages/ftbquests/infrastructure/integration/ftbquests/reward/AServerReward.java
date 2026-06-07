package com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.reward;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.server.level.ServerPlayer;

public class AServerReward extends AGenericReward {
    public AServerReward(long id, Quest quest) {
        super(id, quest);
    }

    public AServerReward(long id, Quest quest, String stage, boolean remove) {
        super(id, quest, stage, remove);
    }

    @Override
    public RewardType getType() {
        return AStagesFTBQuests.SERVER_REWARD_TYPE;
    }

    @Override
    public void claim(ServerPlayer serverPlayer, boolean notify) {
        if (!isRemove()) {
            AStagesUtils.addStage(AHolder.server(), getStage(), !notify);
        } else {
            AStagesUtils.removeStage(AHolder.server(), getStage(), !notify);
        }
    }
}
