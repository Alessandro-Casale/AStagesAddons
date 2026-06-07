package com.alessandro.astages.curios.engine.store;

import com.alessandro.astages.api.store.Attribute;
import com.alessandro.astages.engine.store.AttributeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ACuriosAttributes {
    public static final Attribute<Boolean> CURIO_EQUIPPING = Attribute.create("curio_equipping", AttributeTypes.BOOLEAN, false);;

    public static class Item {
        public static final Attribute<Function<ItemStack, Component>> CURIOS_MESSAGE = Attribute.create("item/curios_message", AttributeTypes.STACK_TO_COMPONENT, stack -> Component.translatable("tooltip.astages_curios.item.curios_integration", stack.getHoverName()).withStyle(ChatFormatting.RED));
    }
}
