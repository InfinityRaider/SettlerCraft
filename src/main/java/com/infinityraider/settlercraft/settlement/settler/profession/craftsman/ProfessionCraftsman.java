package com.infinityraider.settlercraft.settlement.settler.profession.craftsman;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionBase;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ProfessionCraftsman extends ProfessionBase {
    public ProfessionCraftsman() {
        super("craftsMan");
    }

    @Override
    public List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler) {
        return ImmutableList.of();
    }
}
