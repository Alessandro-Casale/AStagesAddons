package com.alessandro.astages.ftbquests.api.util;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.function.Consumer;

public class AFTBTeamsUtils {
    public static void performForPlayerOrTeam(ServerPlayer player, Consumer<UUID> action) {
        var team = FTBTeamsAPI.api()
            .getManager()
            .getTeamForPlayer(player)
            .orElse(null);

        if (team != null) {
            team.getMembers().forEach(action);
        } else {
            action.accept(player.getUUID());
        }
    }
}
