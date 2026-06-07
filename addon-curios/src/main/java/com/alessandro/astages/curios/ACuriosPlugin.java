package com.alessandro.astages.curios;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.plugin.AStagesPlugin;
import com.alessandro.astages.api.plugin.container.AttributeContainer;
import com.alessandro.astages.curios.engine.store.ACuriosAttributes;
import com.alessandro.astages.engine.server.restriction.item.ABaseItemRestriction;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public class ACuriosPlugin implements AStagesPlugin {
    public void attachAttributes(AttributeContainer container) {
        container.addAttribute(ABaseItemRestriction.class, ACuriosAttributes.CURIO_EQUIPPING);
        container.addAttribute(ABaseItemRestriction.class, ACuriosAttributes.Item.CURIOS_MESSAGE);
    }

    public ResourceLocation id() {
        return AResourceLocation.fromNamespaceAndPath("curios");
    }
}
