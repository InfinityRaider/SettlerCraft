package com.infinityraide.settlercraft.settlement.settler.profession;

import com.infinityraide.settlercraft.api.v1.IProfession;
import com.infinityraide.settlercraft.api.v1.IProfessionRegistry;
import com.infinityraide.settlercraft.settlement.settler.profession.blacksmith.ProfessionBlacksmith;
import com.infinityraide.settlercraft.settlement.settler.profession.builder.ProfessionBuilder;
import com.infinityraide.settlercraft.settlement.settler.profession.courier.ProfessionCourier;
import com.infinityraide.settlercraft.settlement.settler.profession.craftsman.ProfessionCraftsman;
import com.infinityraide.settlercraft.settlement.settler.profession.farmer.ProfessionFarmer;
import com.infinityraide.settlercraft.settlement.settler.profession.lumberjack.ProfessionLumberJack;
import com.infinityraide.settlercraft.settlement.settler.profession.miner.ProfessionMiner;
import com.infinityraide.settlercraft.settlement.settler.profession.soldier.ProfessionSoldier;

import java.util.HashMap;
import java.util.Map;

public class ProfessionRegistry implements IProfessionRegistry {
    private static final ProfessionRegistry INSTANCE = new ProfessionRegistry();

    public static ProfessionRegistry getInstance() {
        return INSTANCE;
    }

    private Map<String, IProfession> professions;

    private final IProfession BLACKSMITH;
    private final IProfession BUILDER;
    private final IProfession COURIER;
    private final IProfession CRAFTSMAN;
    private final IProfession LUMBERJACK;
    private final IProfession FARMER;
    private final IProfession MINER;
    private final IProfession SOLDIER;

    private ProfessionRegistry() {
        this.professions = new HashMap<>();

        BLACKSMITH = new ProfessionBlacksmith();
        this.registerProfession(BLACKSMITH);

        BUILDER = new ProfessionBuilder();
        this.registerProfession(BUILDER);

        COURIER = new ProfessionCourier();
        this.registerProfession(COURIER);

        CRAFTSMAN = new ProfessionCraftsman();
        this.registerProfession(CRAFTSMAN);

        LUMBERJACK = new ProfessionLumberJack();
        this.registerProfession(LUMBERJACK);

        FARMER = new ProfessionFarmer();
        this.registerProfession(FARMER);

        MINER = new ProfessionMiner();
        this.registerProfession(MINER);

        SOLDIER = new ProfessionSoldier();
        this.registerProfession(SOLDIER);
    }

    @Override
    public IProfession getProfession(String name) {
        return professions.get(name);
    }

    @Override
    public boolean registerProfession(IProfession profession) {
        if(professions.containsKey(profession.getName())) {
            return false;
        }
        professions.put(profession.getName(), profession);
        return true;
    }

    @Override
    public IProfession professionBlacksmith() {
        return BLACKSMITH;
    }

    @Override
    public IProfession professionBuilder() {
        return BUILDER;
    }

    @Override
    public IProfession professionCourier() {
        return COURIER;
    }

    @Override
    public IProfession professionCraftsman() {
        return CRAFTSMAN;
    }

    @Override
    public IProfession professionLumberJack() {
        return LUMBERJACK;
    }

    @Override
    public IProfession professionFarmer() {
        return FARMER;
    }

    @Override
    public IProfession professionMiner() {
        return MINER;
    }

    @Override
    public IProfession professionSoldier() {
        return SOLDIER;
    }
}
