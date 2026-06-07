package com.alessandro.astages.curios.infrastructure.integration.kubejs.util;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.curios.engine.store.ACuriosAttributes;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

@NotNullParams
public class KubeJSServerUtils {
    public static void setCanBeEquippedInCurioSlots(boolean value, String... restrictionIds) {
        for(String restrictionId : restrictionIds) {
            ARestrictionManager.ITEM_INSTANCE.getRestriction(restrictionId).set(ACuriosAttributes.CURIO_EQUIPPING, value);
        }

    }

    public static void setCurioMessage(Function<ItemStack, Component> value, String... restrictionIds) {
        for(String restrictionId : restrictionIds) {
            ARestrictionManager.ITEM_INSTANCE.getRestriction(restrictionId).set(ACuriosAttributes.Item.CURIOS_MESSAGE, value);
        }

    }
}
