package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class BuildingBase implements IBuilding {
    private final String resourcePath;
    private final String name;

    public BuildingBase(String name) {
        this.resourcePath = this.buildingType().name() + "/" + name;
        this.name = "SettlerCraft.building." + name;
    }

    @Override
    public String name() {
        return name;
    }

    /**
     * This method is used to read json schematics for the buildings, example:
     * new ResourceLocation("settlercraft", "buildings/house/house1") will be converted to "assets/settlercraft/buildings/house/house1.json"
     *
     * @return a ResourceLocation containing the path to the json file defining this building
     */
    @Override
    public ResourceLocation schematicLocation(IBuildingStyle style) {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "buildings/default/" + getResourcePath());
    }

    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public void onBuildingBuilt(World world, ISettlement settlement, ISettlementBuilding building) {

    }
}
