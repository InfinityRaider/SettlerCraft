package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IItemBuildingPlanner;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBuildingPlanner extends ItemBase implements IItemBuildingPlanner {
    public ItemBuildingPlanner() {
        super("buildingPlanner");
        this.setMaxStackSize(1);
        this.setCreativeTab(null);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return !playerIn.isSneaking();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            rotate(stack);
        }
        return stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        ISettlement settlement = getSettlement(stack);
        if(settlement == null || !settlement.isMayor(player)) {
            tooltip.add(StatCollector.translateToLocal(Reference.MOD_ID.toLowerCase() + ".tooltip_planner.invalidMayor"));
        } else {
            IBuilding building = getBuilding(stack);
            if(building != null) {
                tooltip.add(StatCollector.translateToLocal(
                        Reference.MOD_ID.toLowerCase() + ".tooltip_planner.building") +
                        ": " +
                        StatCollector.translateToLocal(building.name()));
            }
        }
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public ISettlement getSettlement(ItemStack stack) {
        if(!isValidStack(stack)) {
            return null;
        }
        if(!stack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if(!tag.hasKey(Names.NBT.SETTLEMENT)) {
            return null;
        }
        int key = tag.getInteger(Names.NBT.SETTLEMENT);
        return SettlementHandler.getInstance().getSettlement(key);
    }

    @Override
    public ItemBuildingPlanner setSettlement(ItemStack stack, ISettlement settlement) {
        if(!isValidStack(stack)) {
            return this;
        }
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(Names.NBT.SETTLEMENT, settlement.id());
        return this;
    }

    @Override
    public IBuilding getBuilding(ItemStack stack) {
        if(!isValidStack(stack)) {
            return null;
        }
        if(!stack.hasTagCompound()) {
            return null;
        }
        NBTTagCompound tag = stack.getTagCompound();
        if(!tag.hasKey(Names.NBT.BUILDINGS)) {
            return null;
        }
        String key = tag.getString(Names.NBT.BUILDINGS);
        return BuildingRegistry.getInstance().getBuildingFromName(key);
    }

    @Override
    public ItemBuildingPlanner setBuilding(ItemStack stack, IBuilding building) {
        if(!isValidStack(stack)) {
            return this;
        }
        if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound tag = stack.getTagCompound();
        tag.setString(Names.NBT.BUILDINGS, building.name());
        return this;
    }

    @Override
    public ItemBuildingPlanner rotate(ItemStack stack) {
        return setRotation(stack, getRotation(stack) + 1);
    }

    @Override
    public int getRotation(ItemStack stack) {
        if(!isValidStack(stack)) {
            return 0;
        }
        return stack.getItemDamage();
    }

    @Override
    public ItemBuildingPlanner setRotation(ItemStack stack, int rotation) {
        if(!isValidStack(stack)) {
            return this;
        }
        stack.setItemDamage(rotation % 4);
        return this;
    }

    private boolean isValidStack(ItemStack stack) {
        return stack != null && stack.getItem() == this;
    }
}
