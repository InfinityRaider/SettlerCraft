package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SettlerAIRoutineGetFood extends SettlerAIRoutineFindResourceAbstract {
    private int foodSlot;

    protected SettlerAIRoutineGetFood(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GETTING_FOOD);
        this.checkSettlerForFoodSlot();
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getHungerStatus().shouldEat();
    }

    @Override
    public void resetRoutine() {
        super.resetRoutine();
        this.checkSettlerForFoodSlot();
    }

    @Override
    public void updateRoutine() {
        if(hasFood()) {
            ItemStack food = getSettler().getSettlerInventory().getStackInSlot(foodSlot);
            ItemStack remaining = getSettler().eatFood(food);
            getSettler().getSettlerInventory().setInventorySlotContents(foodSlot, remaining);
        } else {
            super.updateRoutine();
        }
    }

    @Override
    protected boolean isValidStack(ItemStack stack) {
        return stack != null && (stack.getItem() instanceof ItemFood);
    }

    private boolean hasFood() {
        return foodSlot > 0;
    }

    private void checkSettlerForFoodSlot() {
        int size = getSettler().getSettlerInventory().getSizeInventory();
        for(int i = 0; i < size; i++) {
            if(this.isValidStack(getSettler().getSettlerInventory().getStackInSlot(i))) {
                this.foodSlot = i;
                return;
            }
        }
        this.foodSlot = -1;
    }

    @Override
    public void onInventorySlotChange(ISettler settler, int slot, ItemStack stack) {
        if(settler == getSettler() && isValidStack(stack)) {
            this.foodSlot = slot;
        }
    }
}
