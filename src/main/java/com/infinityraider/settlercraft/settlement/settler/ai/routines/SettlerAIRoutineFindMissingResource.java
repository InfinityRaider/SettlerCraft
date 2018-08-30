package com.infinityraider.settlercraft.settlement.settler.ai.routines;

import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.item.ItemStack;

public class SettlerAIRoutineFindMissingResource extends SettlerAIRoutineFindResourceAbstract {
    public SettlerAIRoutineFindMissingResource(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.FINDING_RESOURCE);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getMissingResource().isPresent();
    }

    @Override
    protected boolean isValidStack(ItemStack stack) {
        if(getSettler().getMissingResource().isPresent()) {
            return getSettler().getMissingResource().get().matches(stack);
        }
        return false;
    }

    @Override
    public void onInventorySlotChange(ISettler settler, int slot, ItemStack stack) {
        if(settler == getSettler() && settler.getMissingResource().isPresent() && isValidStack(stack)) {
            if(getSettler().getMissingResource().get().onResourceAcquired(settler, slot, stack)) {
                getSettler().setMissingResource(null);
            }
        }
    }
}
