package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public abstract class TaskUseItem extends TaskBase {
    private final int slot;
    private final ItemStack stack;

    private int usageTicks;

    public TaskUseItem(String taskName, ISettler settler, int slot) {
        super(taskName, settler);
        this.slot = MathHelper.clamp_int(slot, 0, settler.getSettlerInventory().getSizeInventory());
        this.stack = settler.getSettlerInventory().getStackInSlot(this.slot);
        this.usageTicks = 0;
    }

    private void swapActiveItem() {
        if(slot != 0) {
            getSettler().getSettlerInventory().switchStacksInSlots(0, slot);
        }

    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void startTask() {
        swapActiveItem();
    }

    @Override
    public void updateTask() {
        if(getStack() == null) {
            return;
        }
    }

    @Override
    public void cancelTask() {
        swapActiveItem();
        this.usageTicks = 0;
    }

    @Override
    public boolean isCompleted() {
        return getStack() == null;
    }

    @Override
    public void onTaskCompleted() {
        if(getStack() != null) {
            ActionResult<ItemStack> result = getStack().getItem().onItemRightClick(getStack(), getSettler().getWorld(), getSettler().getFakePlayerImplementation(), EnumHand.MAIN_HAND);
        }
        swapActiveItem();
    }

    protected abstract void onItemUsed(ItemStack stack);

    @Override
    protected void onTaskInterrupted() {
        swapActiveItem();
        this.usageTicks = 0;
    }

    @Override
    protected void onTaskResumed() {
        swapActiveItem();
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        return null;
    }
}
