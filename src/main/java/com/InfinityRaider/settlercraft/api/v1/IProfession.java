package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Interface used to define a settler's profession.
 * Implementations of this interface should be registered in the IProfessionRegistry
 * All professions can be retrieved from the IProfessionRegistry by their name.
 */
public interface IProfession {
    /** Returns a unique name for this profession, this is an unlocalized string, preferably tagged with the mod id to prevent overwriting of other professions */
    String getName();

    /**
     * @param player: player talking to a settler with this profession (can be used to distinguish if the settler is talking to the mayor or not, or insert player name)
     * @return a list of dialogue options specific to this profession, can be empty but should never be null
     */
    List<IDialogueOption> getProfessionSpecificDialogueOptions(EntityPlayer player, ISettler settler);

    /**
     * Returns a ResourceLocation for the texture for this profession, argument is passed to give different textures for male/female and adult/child settlers
     * @param settler the settler with this profession
     * @return ResourceLocation for the texture
     */
    @SideOnly(Side.CLIENT)
    ResourceLocation getEntityTexture(ISettler settler);
}