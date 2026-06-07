package com.alessandro.astages.pufferfish.reward;

import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.util.AStagesUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.puffish.skillsmod.api.SkillsAPI;
import net.puffish.skillsmod.api.json.JsonElement;
import net.puffish.skillsmod.api.json.JsonObject;
import net.puffish.skillsmod.api.reward.Reward;
import net.puffish.skillsmod.api.reward.RewardConfigContext;
import net.puffish.skillsmod.api.reward.RewardDisposeContext;
import net.puffish.skillsmod.api.reward.RewardUpdateContext;
import net.puffish.skillsmod.api.util.Problem;
import net.puffish.skillsmod.api.util.Result;
import net.puffish.skillsmod.util.LegacyUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@NotNullParams
public class AStagesReward implements Reward {
    private static final ResourceLocation ID = AResourceLocation.fromNamespaceAndPath("stage_reward");
    private final String stage;
    private final Operation operation;

    public AStagesReward(String stage, Operation operation) {
        this.stage = stage;
        this.operation = operation;
    }

    public static void register() {
        SkillsAPI.registerReward(ID, AStagesReward::parse);
    }

    private static @Nullable Result<AStagesReward, Problem> parse(RewardConfigContext context) {
        return context.getData().andThen(JsonElement::getAsObject).andThen(LegacyUtils.wrapNoUnused(AStagesReward::parse, context));
    }

    private static Result<AStagesReward, Problem> parse(JsonObject rootObject) {
        var problems = new ArrayList<Problem>();

        var stageJson = rootObject.getString("stage");
        var stage = stageJson.ifFailure(problems::add).getSuccess();

        var operationJson = rootObject.getString("operation");
        var operation = operationJson.ifFailure(problems::add).getSuccess();

        if (problems.isEmpty()) {
            return Result.success(new AStagesReward(
                stage.orElseThrow(),
                parseOperation(operation.orElseThrow())
            ));
        } else {
            return Result.failure(Problem.combine(problems));
        }
    }

    private static Operation parseOperation(String operation) {
        return operation.startsWith("r") ? AStagesReward.Operation.REMOVE : AStagesReward.Operation.ADD;
    }

    public void update(RewardUpdateContext context) {
        ServerPlayer player = context.getPlayer();

        if (context.isAction()) {
            if (this.operation == AStagesReward.Operation.ADD) {
                AStagesUtils.addStage(AHolder.player(player), stage, true);
            } else if (this.operation == AStagesReward.Operation.REMOVE) {
                AStagesUtils.removeStage(AHolder.player(player), stage, true);
            }
        }

    }

    @Override
    public void dispose(RewardDisposeContext rewardDisposeContext) { }

    public enum Operation {
        ADD,
        REMOVE
    }
}