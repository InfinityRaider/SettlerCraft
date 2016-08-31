package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemBuildingPlanner extends ItemWithModelBase implements IItemBuildingPlanner, IItemRenderSettlementBoxes {
    public ItemBuildingPlanner() {
        super("buildingPlanner");
        this.setMaxStackSize(1);
        this.setCreativeTab(null);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(player.isSneaking()) {
            return EnumActionResult.PASS;
        }
        if(!world.isRemote && side == EnumFacing.UP && isValidStack(stack)) {
            BlockPos origin = pos.offset(side);
            ISettlement settlement = SettlementHandler.getInstance().getNearestSettlement(world, origin);
            if(settlement != null) {
                if(buildStructure(stack, player, settlement, origin)) {
                    player.inventory.getCurrentItem().stackSize = player.inventory.getCurrentItem().stackSize - 1;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    private boolean buildStructure(ItemStack stack, EntityPlayer player, ISettlement settlement, BlockPos pos) {
        IBuilding building = getBuilding(stack);
        int rotation = getRotation(stack);
        ISettlementBuilding built = settlement.tryBuildNewBuildingAtLocation(player, building, pos, rotation);
        return built != null;
    }

    @Override
    public boolean isValidBoundingBoxForBuilding(World world, ItemStack stack, EntityPlayer player, ISettlement settlement, IBuilding building, IBoundingBox buildingBox) {
        if(!isValidStack(stack)) {
            return false;
        }
        ISettlement planned = this.getSettlement(world, stack);
        if(planned != settlement) {
            return false;
        }
        if(!settlement.canBuildNewBuilding(building)) {
            return false;
        }
        if(!building.canBuild(player, settlement)) {
            return false;
        }
        if(!settlement.getBoundingBox().intersects(buildingBox)) {
            return false;
        }
        for(ISettlementBuilding built : settlement.getBuildings()) {
            if(built.getBoundingBox().intersects(buildingBox)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking()) {
            rotate(stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        ISettlement settlement = getSettlement(SettlerCraft.proxy.getClientWorld(), stack);
        if(settlement == null || !settlement.isMayor(player)) {
            tooltip.add(I18n.translateToLocal(Reference.MOD_ID.toLowerCase() + ".tooltip_planner.invalidMayor"));
        } else {
            IBuilding building = getBuilding(stack);
            if(building != null) {
                tooltip.add(I18n.translateToLocal(
                        Reference.MOD_ID.toLowerCase() + ".tooltip_planner.building_L1") +
                        I18n.translateToLocal(building.name()));
                tooltip.add(I18n.translateToLocal(
                        Reference.MOD_ID.toLowerCase() + ".tooltip_planner.building_L2"));
                tooltip.add(I18n.translateToLocal(
                        Reference.MOD_ID.toLowerCase() + ".tooltip_planner.building_L3"));
            }
        }
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public ISettlement getSettlement(World world, ItemStack stack) {
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
        return SettlementHandler.getInstance().getSettlement(world, key);
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

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderSettlementBoxes(ISettlement settlement, EntityPlayer player, ItemStack stack) {
        if(!isValidStack(stack)) {
            return false;
        }
        ISettlement planned = this.getSettlement(SettlerCraft.proxy.getClientWorld(), stack);
        return settlement == planned && planned.isMayor(player);
    }

    private boolean isValidStack(ItemStack stack) {
        return stack != null && stack.getItem() == this;
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
