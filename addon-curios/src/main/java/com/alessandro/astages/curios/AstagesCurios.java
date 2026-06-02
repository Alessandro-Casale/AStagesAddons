package com.alessandro.astages.curios;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AstagesCurios.MODID)
public class AstagesCurios {
    public static final String MODID = "astages_curios";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AstagesCurios(IEventBus modEventBus, ModContainer modContainer) { }
}