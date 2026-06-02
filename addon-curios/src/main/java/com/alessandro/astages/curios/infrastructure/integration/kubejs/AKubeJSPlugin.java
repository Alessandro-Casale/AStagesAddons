package com.alessandro.astages.curios.infrastructure.integration.kubejs;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.curios.infrastructure.integration.kubejs.util.KubeJSServerUtils;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;

@NotNullParams
public class AKubeJSPlugin implements KubeJSPlugin {
    public void registerBindings(BindingRegistry registry) {
        registry.add("AStagesCurios", KubeJSServerUtils.class);
    }
}
