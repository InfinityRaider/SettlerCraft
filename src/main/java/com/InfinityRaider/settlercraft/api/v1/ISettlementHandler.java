package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

/**
 * Interface used to interact with the settlement handler,
 * the instance of this interface can be retrieved via api.getSettlementHandler()
 */
public interface ISettlementHandler {
    /**
     * Gets a settlement by its id
     * @param id the id of the settlement
     * @return the settlement with the requested id or null if there is no settlement with this id
     */
    ISettlement getSettlement(int id);

    /**
     * Gets the settlement at the given coordinates in the world, may return null if there is no settlement here
     * (Slow method, if you call this often, cache it instead, settlements don't go anywhere)
     * @param world the world to look for a settlement in
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the settlement at the given position or null if there is no settlement
     */
    ISettlement getSettlementForPosition(World world, double x, double y, double z);

    /**
     * Gets the settlement which has the argument as its home chunk
     * @param chunk the home chunk to get the settlement for
     * @return the settlement in the chunk, or null if there is none
     */
    ISettlement getSettlementForChunk(Chunk chunk);

    /**
     * Gets a list of all settlements in a world
     * @param world the World object
     * @return a list containing all the settlements for the given world, may be empty but should never be null
     */
    List<ISettlement> getSettlementsForWorld(World world);

    /**
     * Checks if all the requirements are met to create a new settlement with the player as mayor at his current position
     * @param player the player creating a new settlement
     * @return if a settlement can be made here
     */
    boolean canCreateSettlementAtCurrentPosition(EntityPlayer player);

    /**
     * Tries to start a new settlement at the player's current position, the player will be the mayor of the new settlement
     * and the settler will be the first builder who will help to build the town hall.
     * This method can fail if the location is too close to an existing settlement, either of the arguments is null
     * or the player does not meet the requirements to create a new settlement here.
     * If the method fails, it will return null instead of the newly created settlement.
     * This method internally calls onCreateSettlementAtCurrentPosition(player)
     *
     * @param player The player creating the settlement with the settler, this will also be the mayor
     * @param settler The first settler creating a new settlement with the player
     * @return the newly created ISettlement object, or null if the player can't make a settlement here.
     */
    ISettlement startNewSettlement(EntityPlayer player, ISettler settler);
}
