package com.infinityraider.settlercraft.settlement.building;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IBuildingStyle;
import com.infinityraider.settlercraft.api.v1.IBuildingStyleRegistry;
import com.infinityraider.settlercraft.reference.Reference;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingStyleRegistry implements IBuildingStyleRegistry {
    private static final BuildingStyleRegistry INSTANCE = new BuildingStyleRegistry();

    public static BuildingStyleRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<String, IBuildingStyle> buildingStyles;

    public final IBuildingStyle DEFAULT;
    public final IBuildingStyle DESERT;

    private BuildingStyleRegistry() {
        this.buildingStyles = new HashMap<>();
        this.DEFAULT = this.registerBuildingStyle(new BuildingStyle("default"));
        this.DESERT = this.registerBuildingStyle(new BuildingStyle("desert"));
    }

    @Override
    public ResourceLocation getSchematicLocation(IBuilding building, IBuildingStyle style) {
        if(style.isBuildingHandled(building)) {
            return style.getSchematicLocation(building);
        } else {
            return building.schematicLocation(style);
        }
    }

    @Override
    public IBuildingStyle getBuildingStyleFromName(String name) {
        IBuildingStyle style = buildingStyles.get(name);
        return style == null ? defaultStyle() : style;
    }

    @Override
    public List<IBuildingStyle> getBuildingStyles() {
        return ImmutableList.copyOf(buildingStyles.values());
    }

    @Override
    public IBuildingStyle registerBuildingStyle(IBuildingStyle style) {
        for(String modId : style.requiredMods()) {
            if(!Loader.isModLoaded(modId)) {
                return null;
            }
        }
        if(!buildingStyles.containsKey(style.getName())) {
            buildingStyles.put(style.getName(), style);
        }
        return buildingStyles.get(style.getName());
    }

    @Override
    public IBuildingStyle defaultStyle() {
        return DEFAULT;
    }

    @Override
    public IBuildingStyle desertStyle() {
        return DESERT;
    }

    public static class BuildingStyle implements IBuildingStyle {
        private final String name;

        private BuildingStyle(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return "settlercraft.style_" + name;
        }

        @Override
        public boolean isBuildingHandled(IBuilding building) {
            return building instanceof BuildingBase;
        }

        @Override
        public ResourceLocation getSchematicLocation(IBuilding building) {
            if(building instanceof BuildingBase) {
                return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "buildings/" + name + "/" + ((BuildingBase) building).getResourcePath());
            } else {
                return building.schematicLocation(this);
            }
        }

        @Override
        public List<String> requiredMods() {
            return ImmutableList.of();
        }
    }

}
