package com.alessandro.astages.curios.infrastructure.integration.kubejs;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.curios.infrastructure.integration.kubejs.util.KubeJSServerUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

@NotNullParams
public class AKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent registry) {
        registry.add("AStagesCurios", KubeJSServerUtils.class);
    }
}
