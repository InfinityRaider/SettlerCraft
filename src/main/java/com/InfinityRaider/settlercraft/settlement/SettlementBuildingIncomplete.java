package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.StructureBuildProgress;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.TaskBuildBuilding;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettlementBuildingIncomplete extends SettlementBuilding {
    private Schematic schematic;
    private StructureBuildProgress buildProgress;
    private BlockPos clicked;

    public SettlementBuildingIncomplete(World world) {
        super(world);
    }

    public SettlementBuildingIncomplete(ISettlement settlement, BlockPos pos, IBuilding building, Schematic schematic, int rotation) {
        super(settlement, schematic.getBoundingBox(pos, rotation), building, rotation, building.getDefaultInventory());
        this.schematic = schematic;
        this.clicked = pos;
        this.buildProgress = new StructureBuildProgress(getWorld(), pos, this.schematic, rotation);
    }

    public StructureBuildProgress getBuildProgress() {
        if(buildProgress == null) {
            this.buildProgress = new StructureBuildProgress(this.getWorld(), this.clicked, this.schematic, this.getRotation());
        }
        return this.buildProgress;
    }

    @Override
    public boolean canDoWorkHere(ISettler settler) {
        return settlement() != null
                && settler != null
                && settler.profession() == ProfessionRegistry.getInstance().BUILDER
                && !getBuildProgress().isComplete();
    }

    @Override
    public ITask getTaskForSettler(ISettler settler) {
        return new TaskBuildBuilding(this.settlement(), settler, this, this.getBuildProgress());
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        return false;
    }

    @Override
    public List<ISettler> inhabitants() {
        return new ArrayList<>();
    }

    @Override
    public BlockPos homePosition() {
        return SchematicRotationTransformer.getInstance().applyRotation(clicked, schematic.home[0], schematic.home[1], schematic.home[2], getRotation());
    }

    @Override
    public boolean isComplete() {
        return false;
    }


    @Override
    public void writeAdditionalDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.X2, clicked.getX());
        tag.setInteger(Names.NBT.Y2, clicked.getY());
        tag.setInteger(Names.NBT.Z2, clicked.getZ());
    }

    @Override
    public void readAdditionalDataFromNBT(NBTTagCompound tag) {
        this.clicked = new BlockPos(tag.getInteger(Names.NBT.X2), tag.getInteger(Names.NBT.Y2), tag.getInteger(Names.NBT.Z2));
        try {
            this.schematic = SchematicReader.getInstance().deserialize(building().schematicLocation());
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
        }
    }
}
