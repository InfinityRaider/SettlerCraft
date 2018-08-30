package com.infinityraider.settlercraft.settlement.settler.profession.builder;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.reference.Reference;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.settlercraft.settlement.settler.dialogue.DialogueOptionBuildBuilding;
import com.infinityraider.settlercraft.settlement.settler.dialogue.DialogueOptionCreateSettlement;
import com.infinityraider.settlercraft.settlement.settler.dialogue.DialogueOptionJoinSettlement;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ProfessionBuilder extends ProfessionBase {
    public ProfessionBuilder() {
        super("builder");
    }

    @Override
    public List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = new ArrayList<>();
        ISettlement home = settler.settlement();
        EntityLiving e = settler.getEntityImplementation();
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(e.getEntityWorld(), e.posX, e.posY+1, e.posZ);

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

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getEntityTexture(ISettler settler) {
        if(settler.isAdult()) {
            if (settler.settlement() != null) {
                return super.getEntityTexture(settler);
            } else {
                String appendix = (settler.isMale() ? "_male" : "_female");
                return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/settler/traveller" + appendix + ".png");
            }
        } else {
            String appendix = (settler.isMale() ? "_male" : "_female");
            return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/settler/child" + appendix + ".png");
        }
    }
}
