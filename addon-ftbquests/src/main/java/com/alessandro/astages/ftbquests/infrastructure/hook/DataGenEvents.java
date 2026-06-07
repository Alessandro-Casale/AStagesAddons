package com.alessandro.astages.ftbquests.infrastructure.hook;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.ftbquests.AStagesFTBQuests;
import com.alessandro.astages.ftbquests.infrastructure.datagen.ALanguageProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@NotNullParams
@Mod.EventBusSubscriber(modid = AStagesFTBQuests.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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