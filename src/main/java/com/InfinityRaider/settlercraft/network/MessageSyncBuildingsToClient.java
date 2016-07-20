package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class MessageSyncBuildingsToClient extends MessageBase<IMessage> {
    private ISettlement settlement;
    private List<NBTTagCompound> buildingTags;

    public MessageSyncBuildingsToClient() {
        super();
        this.buildingTags = new ArrayList<>();
    }

    public MessageSyncBuildingsToClient(ISettlementBuilding... buildings) {
        this();
        if(buildings.length > 0) {
            this.settlement = buildings[0].settlement();
            for(ISettlementBuilding building : buildings) {
                this.buildingTags.add(building.writeBuildingToNBT(new NBTTagCompound()));
            }
        }
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.settlement != null) {
            this.buildingTags.stream().filter(tag -> tag.hasKey(Names.NBT.SLOT)).forEach(tag -> {
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

    @Override
    public void fromBytes(ByteBuf buf) {
        if(buf.readBoolean()) {
            this.settlement = SettlementHandler.getInstance().getSettlement(SettlerCraft.proxy.getClientWorld(), buf.readInt());
            int amount = buf.readInt();
            for(int i = 0; i < amount; i++) {
                this.buildingTags.add(ByteBufUtils.readTag(buf));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if(this.settlement == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeInt(this.settlement.id());
            buf.writeInt(this.buildingTags.size());
            for(NBTTagCompound tag : this.buildingTags) {
                ByteBufUtils.writeTag(buf, tag);
            }
        }
    }
}
