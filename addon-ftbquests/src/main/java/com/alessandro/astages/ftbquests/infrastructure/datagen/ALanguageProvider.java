package com.alessandro.astages.ftbquests.infrastructure.datagen;

import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ALanguageProvider extends LanguageProvider {
    public ALanguageProvider(PackOutput output, String locale) {
        super(output, AStagesFTBQuests.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("ftbquests.reward.astages.stage_description", "Stage: ");
        add("ftbquests.overlay.astages.action.add", "Action: Add Stage");
        add("ftbquests.overlay.astages.action.remove", "Action: Remove Stage");
        add("ftbquests.overlay.astages.placeholder", "Insert stage here...");

        add("ftbquests.reward.astages.player", "AStages Player");
        add("ftbquests.reward.astages.player.stage", "Stage");
        add("ftbquests.reward.astages.player.remove", "Remove");

        add("ftbquests.reward.astages.server", "AStages Server");
        add("ftbquests.reward.astages.server.stage", "Stage");
        add("ftbquests.reward.astages.server.remove", "Remove");
    }
}
