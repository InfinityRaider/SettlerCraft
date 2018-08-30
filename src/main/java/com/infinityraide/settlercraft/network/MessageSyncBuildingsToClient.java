package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.reference.Names;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;

public class MessageSyncBuildingsToClient extends MessageBase<IMessage> {
    private ISettlement settlement;
    private NBTTagCompound[] buildingTags;

    public MessageSyncBuildingsToClient() {
        super();
    }

    public MessageSyncBuildingsToClient(ISettlementBuilding... buildings) {
        this();
        if(buildings.length > 0) {
            this.settlement = buildings[0].settlement();
            this.buildingTags = new NBTTagCompound[buildings.length];
            for(int i = 0; i < buildings.length; i++) {
                this.buildingTags[i] = buildings[i].writeBuildingToNBT(new NBTTagCompound());
            }
        }
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.settlement != null) {
            Arrays.stream(this.buildingTags).filter(tag -> tag.hasKey(Names.NBT.SLOT)).forEach(tag -> {
                ISettlementBuilding building = this.settlement.getBuildingFromId(tag.getInteger(Names.NBT.SLOT));
                if (building != null) {
                    building.readBuildingFromNBT(tag);
                }
            });
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
