package com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.reward;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import com.alessandro.astages.ftbquests.api.util.AFTBTeamsUtils;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraft.server.level.ServerPlayer;

public class APlayerReward extends AGenericReward {
    public APlayerReward(long id, Quest quest) {
        super(id, quest);
    }

    public APlayerReward(long id, Quest quest, String stage, boolean remove) {
        super(id, quest, stage, remove);
    }

    @Override
    public RewardType getType() {
        return AStagesFTBQuests.PLAYER_REWARD_TYPE;
    }

    @Override
    public void claim(ServerPlayer serverPlayer, boolean notify) {
        if (!isRemove()) {
            AFTBTeamsUtils.performForPlayerOrTeam(serverPlayer, uuid ->
                AStagesUtils.addStage(AHolder.player(uuid), getStage(), notify, notify, notify)
            );
        } else {
            AFTBTeamsUtils.performForPlayerOrTeam(serverPlayer, uuid ->
                AStagesUtils.removeStage(AHolder.player(uuid), getStage(), notify, notify, notify)
            );
        }
    }
}
