package com.infinityraider.settlercraft.settlement.building;

import com.infinityraider.settlercraft.api.v1.IInventoryBuilding;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.reference.Names;
import com.infinityraider.infinitylib.utility.inventory.IInventorySerializableItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;

import java.util.*;

public class BuildingInventory implements IInventoryBuilding, IInventorySerializableItemHandler {
    private ISettlementBuilding building;

    private int size;
    private List<IInventory> inventories;
    private Map<IInventory, BlockPos> inventoryToPos;
    private Map<BlockPos, IInventory> posToInventory;

    public BuildingInventory(ISettlementBuilding building) {
        this.reset();
        this.building = building;
    }
    private void reset() {
        this.size = 0;
        this.inventories = new ArrayList<>();
        this.inventoryToPos = new IdentityHashMap<>();
        this.posToInventory = new HashMap<>();
    }

    public void registerInventory(BlockPos pos, IInventory inventory) {
        if(!this.posToInventory.containsKey(pos)) {
            this.size = this.size + inventory.getSizeInventory();
            this.inventories.add(inventory);
            this.inventoryToPos.put(inventory, pos);
            this.posToInventory.put(pos, inventory);
        }
    }

    @Override
    public BlockPos getPositionForInventory(IInventory inventory){
        return this.inventoryToPos.get(inventory);
    }

    @Override
    public IInventory getInventoryForPosition(BlockPos pos) {
        return this.posToInventory.get(pos);
    }

    @Override
    public Tuple<IInventory, Integer> getInventoryAndSlotForGlobalSlot(int slot) {
        if(slot >= this.getSizeInventory()) {
            return null;
        }
        for (IInventory inventory : this.inventories) {
            int size = inventory.getSizeInventory();
            if (slot >= size) {
                slot = slot - size;
            } else {
                return new Tuple<>(inventory, slot);
            }
        }
        return null;
    }

    @Override
    public int getSizeInventory() {
        return this.size;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        Tuple<IInventory, Integer> inventoryAndSlot = this.getInventoryAndSlotForGlobalSlot(index);
        return inventoryAndSlot == null ? null : inventoryAndSlot.getFirst().getStackInSlot(inventoryAndSlot.getSecond());
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        Tuple<IInventory, Integer> inventoryAndSlot = this.getInventoryAndSlotForGlobalSlot(index);
        if(inventoryAndSlot != null) {
            inventoryAndSlot.getFirst().markDirty();
            return inventoryAndSlot.getFirst().decrStackSize(inventoryAndSlot.getSecond(), count);
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        Tuple<IInventory, Integer> inventoryAndSlot = this.getInventoryAndSlotForGlobalSlot(index);
        if(inventoryAndSlot != null) {
            inventoryAndSlot.getFirst().markDirty();
            return inventoryAndSlot.getFirst().removeStackFromSlot(inventoryAndSlot.getSecond());
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        Tuple<IInventory, Integer> inventoryAndSlot = this.getInventoryAndSlotForGlobalSlot(index);
        if(inventoryAndSlot != null) {
            inventoryAndSlot.getFirst().setInventorySlotContents(inventoryAndSlot.getSecond(), stack);
            inventoryAndSlot.getFirst().markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        this.inventories.forEach(IInventory :: markDirty);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        Tuple<IInventory, Integer> inventoryAndSlot = this.getInventoryAndSlotForGlobalSlot(index);
        return inventoryAndSlot != null && inventoryAndSlot.getFirst().isItemValidForSlot(inventoryAndSlot.getSecond(), stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {}

    @Override
    public String getName() {
        return "settlercraft.inventory.building";
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(I18n.translateToLocal(getName()));
    }

    @Override
    public NBTTagCompound writeToNBT() {
        return this.writeInventoryToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.readInventoryFromNBT(tag);
    }

    @Override
    public ISettlementBuilding getBuilding() {
        return this.building;
    }

    @Override
    public NBTTagCompound writeInventoryToNBT(NBTTagCompound tag) {
        NBTTagList tagList = new NBTTagList();
        for (IInventory inventory : inventories) {
            BlockPos pos = this.getPositionForInventory(inventory);
            NBTTagCompound subTag = new NBTTagCompound();
            subTag.setIntArray(Names.NBT.INVENTORY, new int[]{pos.getX(), pos.getY(), pos.getZ()});
            tagList.appendTag(subTag);
        }
        tag.setTag(Names.NBT.INVENTORY, tagList);
        return tag;
    }

    @Override
    public NBTTagCompound readInventoryFromNBT(NBTTagCompound tag) {
        this.reset();
        NBTTagList list = tag.hasKey(Names.NBT.INVENTORY) ? tag.getTagList(Names.NBT.INVENTORY, 10) : null;
        if(list != null) {
            int amount = list.tagCount();
            this.inventories = new ArrayList<>();
            for(int i = 0; i < amount; i ++) {
                NBTTagCompound dataTag = list.getCompoundTagAt(i);
                int[] data = dataTag.hasKey(Names.NBT.INVENTORY) ? dataTag.getIntArray(Names.NBT.INVENTORY) : null;
                if(data == null || data.length < 3) {
                    continue;
                }
                BlockPos pos = new BlockPos(data[0], data[1], data[2]);
                TileEntity tile = getBuilding().getWorld().getTileEntity(pos);
                if(tile == null || !(tile instanceof IInventory)) {
                    continue;
                }
                IInventory inventory = (IInventory) tile;
                this.inventories.add(inventory);
                this.size = this.size + inventory.getSizeInventory();
                this.posToInventory.put(pos, inventory);
                this.inventoryToPos.put(inventory, pos);
            }
        }
        return tag;
    }
}
