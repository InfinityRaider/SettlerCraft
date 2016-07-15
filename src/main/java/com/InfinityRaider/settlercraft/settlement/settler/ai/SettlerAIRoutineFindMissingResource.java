package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.item.ItemStack;

public class SettlerAIRoutineFindMissingResource extends SettlerAIRoutineFindResourceAbstract {
    protected SettlerAIRoutineFindMissingResource(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.FINDING_RESOURCE);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getMissingResource() != null;
    }

    @Override
    protected boolean isValidStack(ItemStack stack) {
        return ItemStack.areItemsEqual(stack, getSettler().getMissingResource())
                && ItemStack.areItemStackTagsEqual(stack, getSettler().getMissingResource());
    }

    @Override
    protected void postResourceFind() {}

    @Override
    public void onInventorySlotChange(ISettler settler, int slot, ItemStack stack) {
        if(settler == getSettler() && isValidStack(stack)) {
            getSettler().setMissingResource(null);
        }
    }
}
