package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.IProfession;
import com.InfinityRaider.settlercraft.settlement.settler.profession.blacksmith.ProfessionBlacksmith;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.ProfessionBuilder;
import com.InfinityRaider.settlercraft.settlement.settler.profession.courier.ProfessionCourier;
import com.InfinityRaider.settlercraft.settlement.settler.profession.craftsman.ProfessionCraftsman;
import com.InfinityRaider.settlercraft.settlement.settler.profession.farmer.ProfessionFarmer;
import com.InfinityRaider.settlercraft.settlement.settler.profession.miner.ProfessionMiner;
import com.InfinityRaider.settlercraft.settlement.settler.profession.soldier.ProfessionSoldier;

import java.util.HashMap;
import java.util.Map;

public class ProfessionRegistry {
    private static final ProfessionRegistry INSTANCE = new ProfessionRegistry();

    public static ProfessionRegistry getInstance() {
        return INSTANCE;
    }

    private Map<String, IProfession> professions;

    public final IProfession BLACKSMITH;
    public final IProfession BUILDER;
    public final IProfession COURIER;
    public final IProfession CRAFTSMAN;
    public final IProfession FARMER;
    public final IProfession MINER;
    public final IProfession SOLDIER;

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

        FARMER = new ProfessionFarmer();
        this.registerProfession(FARMER);

        MINER = new ProfessionMiner();
        this.registerProfession(MINER);

        SOLDIER = new ProfessionSoldier();
        this.registerProfession(SOLDIER);
    }

    public IProfession getProfession(String name) {
        return professions.get(name);
    }

    public boolean registerProfession(IProfession profession) {
        if(professions.containsKey(profession.getName())) {
            return false;
        }
        professions.put(profession.getName(), profession);
        return true;
    }
}
