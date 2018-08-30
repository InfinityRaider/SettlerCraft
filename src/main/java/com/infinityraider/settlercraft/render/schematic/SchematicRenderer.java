package com.infinityraider.settlercraft.render.schematic;

import com.infinityraider.settlercraft.SettlerCraft;
import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IBuildingStyle;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.item.ItemBuildingPlanner;
import com.infinityraider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.infinityraider.settlercraft.utility.BoundingBox;
import com.infinityraider.settlercraft.utility.schematic.Schematic;
import com.infinityraider.settlercraft.utility.schematic.SchematicReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class SchematicRenderer {
    private static final SchematicRenderer INSTANCE = new SchematicRenderer();

    public static SchematicRenderer getInstance() {
        return INSTANCE;
    }

    private SchematicWorld currentSchematic;
    private String name = "";

    private SchematicRenderer() {}

    public void doRender(BufferBuilder buffer) {
        if(this.hasSchematic()) {
            for(int x = 0; x < currentSchematic.sizeX(); x++) {
                for(int y = 0; y < currentSchematic.sizeY(); y++) {
                    for(int z = 0; z < currentSchematic.sizeZ(); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(currentSchematic.getBlockState(pos), pos, currentSchematic, buffer);
                    }
                }
            }
        }
    }

    public void setSchematicFromStack(ItemStack stack, ItemBuildingPlanner planner) {
        IBuilding building = planner.getBuilding(stack);
        if(building == null) {
            this.currentSchematic = null;
            this.name = "";
            return;
        }
        ISettlement settlement = planner.getSettlement(SettlerCraft.proxy.getClientWorld(), stack);
        if(settlement == null || !settlement.isMayor(Minecraft.getMinecraft().player)) {
            this.currentSchematic = null;
            this.name = "";
            return;
        }
        if(building.name().equals(this.name)) {
            return;
        }
        setCurrentSchematic(building, settlement.getBuildingStyle());
    }

    public void setCurrentSchematic(IBuilding building, IBuildingStyle style) {
        Schematic schematic;
        try {
            schematic = SchematicReader.getInstance().deserialize(BuildingStyleRegistry.getInstance().getSchematicLocation(building, style));
        } catch (IOException e) {
            SettlerCraft.instance.getLogger().printStackTrace(e);
            return;
        }
        this.currentSchematic = new SchematicWorld(Minecraft.getMinecraft().world, new BlockPos(0, 0, 0), schematic, 0);
        this.name = building.name();
    }

    public SchematicRenderer setOrigin(BlockPos pos) {
        if(hasSchematic()) {
            currentSchematic.setOrigin(pos);
        }
        return this;
    }

    public BoundingBox getBoundingBox() {
        if(hasSchematic()) {
            return currentSchematic.getBoundingBox();
        }
        return null;
    }

    public boolean hasSchematic() {
        return currentSchematic != null;
    }

}
