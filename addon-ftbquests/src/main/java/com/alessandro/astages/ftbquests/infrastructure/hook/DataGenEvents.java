package com.alessandro.astages.ftbquests.infrastructure.hook;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import com.alessandro.astages.ftbquests.infrastructure.datagen.ALanguageProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@NotNullParams
@EventBusSubscriber(modid = AStagesFTBQuests.MODID)
public class DataGenEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.getGenerator().addProvider(
            event.includeClient(),
            new ALanguageProvider(packOutput, "en_us")
        );
    }
}