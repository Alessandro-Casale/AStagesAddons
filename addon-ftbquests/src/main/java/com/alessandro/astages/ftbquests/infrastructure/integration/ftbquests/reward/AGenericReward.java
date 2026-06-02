package com.alessandro.astages.ftbquests.infrastructure.integration.ftbquests.reward;

import com.alessandro.astages.engine.AClientStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardAutoClaim;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class AGenericReward extends Reward {
    private static final String STAGE_KEY = "stage";
    private static final String REMOVE_KEY = "remove";

    private String stage = "";
    private boolean remove = false;

    public AGenericReward(long id, Quest quest) {
        super(id, quest);
        this.autoclaim = RewardAutoClaim.INVISIBLE;
    }

    public AGenericReward(long id, Quest quest, String stage, boolean remove) {
        this(id, quest);
        this.stage = stage;
        this.remove = remove;
    }

    @Override
    public void readData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.readData(nbt, provider);
        stage = nbt.getString(STAGE_KEY);
        remove = nbt.getBoolean(REMOVE_KEY);
    }

    @Override
    public void writeData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.writeData(nbt, provider);
        nbt.putString(STAGE_KEY, stage);
        nbt.putBoolean(REMOVE_KEY, remove);
    }

    @Override
    public void readNetData(RegistryFriendlyByteBuf buffer) {
        super.readNetData(buffer);
        stage = buffer.readUtf();
        remove = buffer.readBoolean();
    }

    @Override
    public void writeNetData(RegistryFriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(stage);
        buffer.writeBoolean(remove);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString(STAGE_KEY, stage, value -> stage = value, "");
        config.addBool(REMOVE_KEY, remove, value -> remove = value, false);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Component getAltTitle() {
        return Component.translatable("ftbquests.reward.astages.stage_description").append(Component.literal(stage).withStyle(remove ? ChatFormatting.RED : ChatFormatting.GREEN));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon getAltIcon() {
        var restriction = AClientStageManager.GENERIC_INSTANCE.getStage(stage);

        if (restriction != null && !restriction.isValueNull(StageAttributes.ICON)) {
            var stack = restriction.get(StageAttributes.ICON);
            return ItemIcon.getItemIcon(stack);
        }

        return super.getAltIcon();
    }

    @Override
    public boolean ignoreRewardBlocking() {
        return true;
    }

    @Override
    protected boolean isIgnoreRewardBlockingHardcoded() {
        return true;
    }

    public String getStage() {
        return stage;
    }

    public boolean isRemove() {
        return remove;
    }
}
