package com.alessandro.astages.ftbquests;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AStagesFTBQuests.MODID)
public class AStagesFTBQuests {
    public static final String MODID = "astages_ftbquests";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static RewardType PLAYER_REWARD_TYPE;
    public static RewardType SERVER_REWARD_TYPE;

    public AStagesFTBQuests() { }
}
