package com.alessandro.astages.curios.infrastructure.hook;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.curios.AstagesCurios;
import com.alessandro.astages.curios.engine.store.ACuriosAttributes;
import com.alessandro.astages.engine.ARestrictionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioEquipEvent;

@NotNullParams
@Mod.EventBusSubscriber(modid = AstagesCurios.MODID)
public class ItemServerEvents {
    @SubscribeEvent
    public static void onCurioEquip(CurioEquipEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof ServerPlayer player) {
            ItemStack curio = event.getStack();
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), curio);

            if (restriction != null && restriction.isDisabled(ACuriosAttributes.CURIO_EQUIPPING)) {
                event.setResult(Event.Result.DENY);
                restriction.displayMessage(ACuriosAttributes.Item.CURIOS_MESSAGE, curio, player);
            }
        }

    }
}
