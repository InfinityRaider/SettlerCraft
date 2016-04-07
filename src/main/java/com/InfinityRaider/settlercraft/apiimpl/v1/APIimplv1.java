package com.InfinityRaider.settlercraft.apiimpl.v1;

import com.InfinityRaider.settlercraft.api.API;
import com.InfinityRaider.settlercraft.api.APIBase;
import com.InfinityRaider.settlercraft.api.APIStatus;
import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.registry.EntityRegistry;
import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionCreator;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.util.math.BlockPos;

public class APIimplv1  implements APIv1 {
    private final int version;
    private final APIStatus status;

    public APIimplv1(int version, APIStatus status) {
        this.version = version;
        this.status = status;
    }

    @Override
    public APIBase getAPI(int maxVersion) {
        if (maxVersion == version && status == APIStatus.OK) {
            return this;
        } else {
            return API.getAPI(maxVersion);
        }
    }

    @Override
    public APIStatus getStatus() {
        return status;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public ISettlerCraftBlockRegistry getBlockRegistry() {
        return BlockRegistry.getInstance();
    }

    @Override
    public ISettlerCraftItemRegistry getItemRegistry() {
        return ItemRegistry.getInstance();
    }

    @Override
    public ISettlerCraftEntityRegistry getEntityRegistry() {
        return EntityRegistry.getInstance();
    }

    @Override
    public ISchematicRotationTransformer getSchematicRotationTransformer() {
        return SchematicRotationTransformer.getInstance();
    }

    @Override
    public IBuildingRegistry getBuildingRegistry() {
        return BuildingRegistry.getInstance();
    }

    @Override
    public IBuildingTypeRegistry getBuildingTypeRegistry() {
        return BuildingTypeRegistry.getInstance();
    }

    @Override
    public IBuildingStyleRegistry getBuildingStyleRegistry() {
        return BuildingStyleRegistry.getInstance();
    }

    @Override
    public ISettlementHandler getSettlementHandler() {
        return SettlementHandler.getInstance();
    }

    @Override
    public IDialogueOptionCreator getDialogueOptionCreator() {
        return DialogueOptionCreator.getInstance();
    }

    @Override
    public IProfession getProfessionFromName(String name) {
        return ProfessionRegistry.getInstance().getProfession(name);
    }

    @Override
    public IBoundingBox createNewBoundingBox(BlockPos min, BlockPos max) {
        return new BoundingBox(min, max);
    }

    @Override
    public IBoundingBox createNewBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
