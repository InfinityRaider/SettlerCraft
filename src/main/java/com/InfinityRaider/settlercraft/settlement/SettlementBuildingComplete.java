package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class SettlementBuildingComplete extends SettlementBuilding {
    private List<EntitySettler> inhabitants;
    private BlockPos home;

    public SettlementBuildingComplete() {
        super();
        this.inhabitants = new ArrayList<>();
    }

    public SettlementBuildingComplete(SettlementBuildingIncomplete incomplete) {
        super(incomplete.settlement(), incomplete.getBoundingBox(), incomplete.building(), incomplete.getRotation(), incomplete.inventory());
        this.inhabitants = new ArrayList<>();
        this.home = incomplete.homePosition();
    }

    @Override
    public boolean canDoWorkHere(ISettler settler) {
        return building().canSettlerWorkHere(this, settler);
    }

    @Override
    public List<EntitySettler> inhabitants() {
        return inhabitants;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public BlockPos homePosition() {
        return home;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = super.writeToNBT();
        tag.setInteger(Names.NBT.X2, home.getX());
        tag.setInteger(Names.NBT.Y2, home.getY());
        tag.setInteger(Names.NBT.Z2, home.getZ());
        int[] settlers = new int[inhabitants.size()];
        for(int i = 0; i < settlers.length; i++) {
            settlers[i] = inhabitants.get(i).getEntityId();
        }
        tag.setIntArray(Names.NBT.SETTLERS, settlers);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.home = new BlockPos(tag.getInteger(Names.NBT.X2), tag.getInteger(Names.NBT.Y2), tag.getInteger(Names.NBT.Z2));
        int[] ids = tag.getIntArray(Names.NBT.SETTLERS);
        for(int id : ids) {
            Entity entity = SettlerCraft.proxy.getEntityById(settlement().world(), id);
            if(entity != null && entity instanceof EntitySettler) {
                inhabitants.add((EntitySettler) entity);
            }
        }
    }
}
