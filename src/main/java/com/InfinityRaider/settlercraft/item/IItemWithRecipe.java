package com.InfinityRaider.settlercraft.item;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public interface IItemWithRecipe {
    List<IRecipe> getRecipes();
}
