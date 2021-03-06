package com.infinityraider.settlercraft.settlement.settler;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.IMissingResource;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collections;
import java.util.List;

public class MissingResourceStack implements IMissingResource {
    private final ItemStack stack;

    public MissingResourceStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean matches(ItemStack stack) {
        return ItemStack.areItemsEqual(stack, this.stack) && ItemStack.areItemStackTagsEqual(stack, this.stack);
    }

    @Override
    public boolean onResourceAcquired(ISettler settler, int slot, ItemStack stack) {
        if(this.stack.getCount() <= stack.getCount()) {
            return true;
        }
        this.stack.setCount(this.stack.getCount() - stack.getCount());
        return false;
    }

    @Override
    public List<ItemStack> getDefaultStacks() {
        return ImmutableList.of(this.stack);
    }

    @Override
    public List<ITextComponent> getDescription(ISettler settler) {
        return ImmutableList.of(new TextComponentTranslation("settlercraft.dialogue.task.findingResource")
                .appendSibling(new TextComponentString(" " + this.stack.getDisplayName())));
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(ISettler settler, EntityPlayer player) {
        return Collections.emptyList();
    }
}
