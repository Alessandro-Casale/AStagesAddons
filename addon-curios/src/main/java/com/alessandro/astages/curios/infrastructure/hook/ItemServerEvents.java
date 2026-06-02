package com.alessandro.astages.curios.infrastructure.hook;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.curios.AstagesCurios;
import com.alessandro.astages.curios.engine.store.ACuriosAttributes;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;

@NotNullParams
@EventBusSubscriber(modid = AstagesCurios.MODID)
public class ItemServerEvents {
    @SubscribeEvent
    public static void onCurioEquip(CurioCanEquipEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof ServerPlayer player) {
            ItemStack curio = event.getStack();
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), curio);

            if (restriction != null && restriction.isDisabled(ACuriosAttributes.CURIO_EQUIPPING)) {
                event.setEquipResult(TriState.FALSE);
                restriction.displayMessage(ACuriosAttributes.Item.CURIOS_MESSAGE, curio, player);
            }
        }

    }
}
