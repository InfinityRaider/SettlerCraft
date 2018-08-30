package com.infinityraide.settlercraft.api.v1;

/**
 * Interface to access the SettlerCraft professions,
 * this should not be implemented by you.
 * The instance can by retrieved using APIv1.getProfessionRegistry()
 */
@SuppressWarnings("unused")
public interface IProfessionRegistry {
    /**
     * Gets a profession based on a name
     * @param name name of the profession
     * @return the profession with the requested name, or null if no such profession is registered
     */
    IProfession getProfession(String name);

    /**
     * Registers a new profession
     * @param profession the profession to be registered
     * @return true if registering was succesful
     */
    boolean registerProfession(IProfession profession);

    /**
     * @return SettlerCraft blacksmith profession
     */
    IProfession professionBlacksmith();

    /**
     * @return SettlerCraft builder profession
     */
    IProfession professionBuilder();

    /**
     * @return SettlerCraft courier profession
     */
    IProfession professionCourier();

    /**
     * @return SettlerCraft craftsman profession
     */
    IProfession professionCraftsman();

    /**
     * @return SettlerCraft lumberjack profession
     */
    IProfession professionLumberJack();

    /**
     * @return SettlerCraft farmer profession
     */
    IProfession professionFarmer();

    /**
     * @return SettlerCraft miner profession
     */
    IProfession professionMiner();

    /**
     * @return SettlerCraft soldier profession
     */
    IProfession professionSoldier();
}
