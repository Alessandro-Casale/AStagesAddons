package com.alessandro.astages.pufferfish;

import com.alessandro.astages.pufferfish.reward.AStagesReward;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AStagesPufferfish.MODID)
public class AStagesPufferfish {
    public static final String MODID = "astages_pufferfish";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AStagesPufferfish() {
        LOGGER.info("Started AStages-PufferFish Skill's reward register!");
        AStagesReward.register();
        LOGGER.info("Finished AStages-PufferFish Skill's reward register!");
    }
}
