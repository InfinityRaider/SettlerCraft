package com.InfinityRaider.settlercraft.settlement.settler.ai.routines;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SettlerAIRoutineFindResourceAbstract extends SettlerAIRoutine implements IInventorySettler.IListener {
    private List<ISettlementBuilding> buildingsToCheck;
    private ISettlementBuilding current;
    private BlockPos target;
    private int index;
    private IInventory inventory;
    private boolean pathFinding;

    public SettlerAIRoutineFindResourceAbstract(EntitySettler settler, ISettler.SettlerStatus status) {
        super(settler, status);
        this.findRelevantBuildings(settler);
        this.index = -1;
        this.pathFinding = false;
        settler.getSettlerInventory().registerInventoryListener(this);
    }

    private void findRelevantBuildings(ISettler settler) {
        this.buildingsToCheck = new ArrayList<>();
        ISettlementBuilding workPlace = settler.workPlace();
        if(workPlace != null) {
            this.buildingsToCheck.add(workPlace);
        }
        ISettlementBuilding home = settler.home();
        if(home != null) {
            this.buildingsToCheck.add(home);
        }
        ISettlement settlement = settler.settlement();
        if(settlement != null) {
            buildingsToCheck.addAll(settlement.getCompletedBuildings().stream().filter(
                    building -> building.building().buildingType() == BuildingTypeRegistry.getInstance().buildingTypeTownHall() ||
                            building.building().buildingType() == BuildingTypeRegistry.getInstance().buildingTypeWareHouse()).collect(Collectors.toList()));
        }
        this.updateBuildingToSearch();
    }

    private void updateBuildingToSearch() {
        if(buildingsToCheck.size() > 0) {
            this.current = this.buildingsToCheck.get(0);
            this.buildingsToCheck.remove(0);
        } else {
            this.current = null;
        }
    }

    @Override
    public boolean continueExecutingRoutine() {
        return shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {

    }

    @Override
    public void interruptRoutine() {
        this.buildingsToCheck = new ArrayList<>();
        this.current = null;
        this.target = null;
        this.index = -1;
        this.inventory = null;
        this.pathFinding = false;
        if(continueExecutingRoutine()) {
            this.findRelevantBuildings(getSettler());
        }
    }

    @Override
    public void updateRoutine() {
        //if there are no more buildings to check, move the settler to the town hall
        if(current == null) {
            ISettlementBuilding townHall = getSettler().settlement().getTownHall();
            if(townHall != null && !pathFinding) {
                target = townHall.homePosition();
                this.findPathToTarget();
            }
            return;
        }
        //move to the current target, if it is a building start searching the inventory, if it is an inventory, retrieve the stack
        if(index < 0) {
            if(target == null) {
                target = current.homePosition();
                this.findPathToTarget();
            } else if(!pathFinding) {
                this.findPathToTarget();
            }
            if(target != null) {
                double dist = this.getDistanceFromPositionSquared(target);
                double reach = 2.5;
                if(dist <= reach * reach) {
                    this.pathFinding = false;
                    if(this.inventory == null) {
                        index = 0;
                    } else {
                        ItemStack inInventory = inventory.getStackInSlot(index);
                        ItemStack remaining = this.getSettler().getSettlerInventory().addStackToInventory(inInventory.copy());
                        if(remaining == null || remaining.stackSize <= 0) {
                            inventory.setInventorySlotContents(index, null);
                        } else {
                            inventory.decrStackSize(index, inInventory.stackSize - remaining.stackSize);
                        }
                        this.interruptRoutine();
                    }
                }
            }
        }
        //search the building's inventory, if the building has the required stack, continue, else move on to the next building
        if(index >= 0 && current != null) {
            IInventoryBuilding inv = current.inventory();
            if(index < inv.getSizeInventory()) {
                if(isValidStack(inv.getStackInSlot(index))) {
                    this.inventory = inv;
                } else {
                    index = index + 1;
                }
            } else {
                this.updateBuildingToSearch();
                index = -1;
            }
        }
    }

    private void findPathToTarget() {
        this.findPathToTarget(target);
        this.pathFinding = true;
    }

    protected abstract boolean isValidStack(ItemStack stack);
}
