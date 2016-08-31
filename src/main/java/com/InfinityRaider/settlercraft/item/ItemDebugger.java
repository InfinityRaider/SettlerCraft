package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.api.v1.IItemRenderSettlementBoxes;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.utility.debug.*;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemDebuggerBase;
import com.infinityraider.infinitylib.utility.debug.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase implements IItemWithModel, IItemRenderSettlementBoxes {
    private static final DebugMode[] DEBUG_MODES = {
            new DebugModeBuildSchematic(),
            new DebugModeFinishBuilding(),
            new DebugModeSettlementInfo(),
            new DebugModeResetBuilding(),
            new DebugModePathfinding()
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
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && player.isSneaking()) {
            DebugMode mode = this.changeDebugMode(stack);
            player.addChatComponentMessage(new TextComponentString("Set debug mode to " + mode.debugName()));
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking()) {
            this.getDebugMode(stack).debugAction(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    private DebugMode getDebugMode(ItemStack stack) {
        NBTTagCompound tag;
        if(!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        } else {
            tag = stack.getTagCompound();
        }
        if(!tag.hasKey(Names.NBT.COUNT)) {
            tag.setInteger(Names.NBT.COUNT, 0);
        }
        return DEBUG_MODES[tag.getInteger(Names.NBT.COUNT) % DEBUG_MODES.length];
    }

    private DebugMode changeDebugMode(ItemStack stack) {
        NBTTagCompound tag;
        if(!stack.hasTagCompound()) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        } else {
            tag = stack.getTagCompound();
        }
        int index;
        if(!tag.hasKey(Names.NBT.COUNT)) {
            index = 1;
        } else {
            index = (tag.getInteger(Names.NBT.COUNT) + 1 ) % DEBUG_MODES.length;
        }
        tag.setInteger(Names.NBT.COUNT, index);
        return DEBUG_MODES[index];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderSettlementBoxes(ISettlement settlement, EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase()+ ":" + getInternalName(), "inventory")));
        return list;
    }
}
