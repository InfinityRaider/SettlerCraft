package com.infinityraider.settlercraft.item;

import com.infinityraider.settlercraft.api.v1.IItemRenderSettlementBoxes;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.reference.Reference;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemDebuggerBase;
import com.infinityraider.infinitylib.utility.debug.*;
import com.infinityraider.settlercraft.utility.debug.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase implements IItemWithModel, IItemRenderSettlementBoxes {
    private static final DebugMode[] DEBUG_MODES = {
            new DebugModeBuildSchematic(),
            new DebugModeFinishBuilding(),
            new DebugModeSettlementInfo(),
            new DebugModeResetBuilding(),
            new DebugModePathfinding(),
            new DebugModeSettlerPathing()
    };

    public ItemDebugger() {
        super(true);
        this.setMaxStackSize(1);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<DebugMode> getDebugModes() {
        return Arrays.asList(DEBUG_MODES);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderSettlementBoxes(ISettlement settlement, EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase()+ ":" + getInternalName(), "inventory")));
        return list;
    }
}
