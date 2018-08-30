package com.infinityraide.settlercraft.settlement.settler.profession.lumberjack;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.settlement.settler.profession.ProfessionBase;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ProfessionLumberJack extends ProfessionBase {
    public ProfessionLumberJack() {
        super("lumberJack");
    }

    @Override
    public List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler) {
        return ImmutableList.of();
    }
}
