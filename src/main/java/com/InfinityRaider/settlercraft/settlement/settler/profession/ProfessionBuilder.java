package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class ProfessionBuilder extends ProfessionBase {
    protected ProfessionBuilder() {
        super("builder");
    }

    @Override
    public List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = new ArrayList<>();
        ISettlement home = settler.settlement();
        EntityLiving e = settler.getEntityImplementation();
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(e.worldObj, e.posX, e.posY+1, e.posZ);

        if(home == null) {
            if(settlement != null) {
                if(settlement.isMayor(player)) {
                    list.add(new DialogueOptionJoinSettlement(player, settler, settlement));
                }
            } else if(SettlementHandler.getInstance().canCreateSettlementAtCurrentPosition(player)) {
                list.add(new DialogueOptionCreateSettlement(player, settler));
            }
        } else {
            if(settlement != null && settlement.isMayor(player)) {
                list.add(new DialogueOptionBuildBuilding(player, settler));
            }
        }
        return list;
    }
}
