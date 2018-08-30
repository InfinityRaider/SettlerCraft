package com.infinityraide.settlercraft.settlement.settler.profession.blacksmith;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.settlement.settler.profession.ProfessionBase;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ProfessionBlacksmith extends ProfessionBase {
    public ProfessionBlacksmith() {
        super("blackSmith");
    }

    @Override
    public List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler) {
        return ImmutableList.of();
    }
}
