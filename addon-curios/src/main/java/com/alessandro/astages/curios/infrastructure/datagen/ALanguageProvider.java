package com.alessandro.astages.curios.infrastructure.datagen;

import com.alessandro.astages.curios.AstagesCurios;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ALanguageProvider extends LanguageProvider {
    public ALanguageProvider(PackOutput output, String locale) {
        super(output, AstagesCurios.MODID, locale);
    }

    protected void addTranslations() {
        this.add("tooltip.astages_curios.item.curios_integration", "Unfamiliar Curio");
    }
}