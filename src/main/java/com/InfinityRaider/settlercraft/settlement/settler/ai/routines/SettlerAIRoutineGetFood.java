package com.InfinityRaider.settlercraft.settlement.settler.ai.routines;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskUseItem;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class SettlerAIRoutineGetFood extends SettlerAIRoutineFindResourceAbstract {
    private int foodSlot;

    public SettlerAIRoutineGetFood(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GETTING_FOOD);
        this.checkSettlerForFoodSlot();
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getHungerStatus().shouldEat();
    }

    @Override
    public void interruptRoutine() {
        super.interruptRoutine();
        this.checkSettlerForFoodSlot();
    }

    @Override
    public void updateRoutine() {
        if(hasFood()) {
            ItemStack food = getSettler().getSettlerInventory().getStackInSlot(foodSlot);
            getSettler().assignTask(new TaskUseItem("eatFood", getSettler(), foodSlot, 2, food.getMaxItemUseDuration()));
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
