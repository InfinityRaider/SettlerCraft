package com.infinityraider.settlercraft.settlement.building;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IBuildingStyle;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.reference.Reference;
import net.minecraft.util.ResourceLocation;

public abstract class BuildingBase implements IBuilding {
    private final String resourcePath;
    private final String name;

    public BuildingBase(String name) {
        this.resourcePath = this.buildingType().name() + "/" + name;
        this.name = Reference.MOD_ID.toLowerCase() + ".building." + name;
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
    public void onBuildingCompleted(ISettlementBuilding building) {}
}
