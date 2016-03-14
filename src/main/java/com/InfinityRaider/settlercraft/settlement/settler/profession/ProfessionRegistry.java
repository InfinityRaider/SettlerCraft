package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.IProfession;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.ProfessionBuilder;

import java.util.HashMap;
import java.util.Map;

public class ProfessionRegistry {
    private static final ProfessionRegistry INSTANCE = new ProfessionRegistry();

    public static ProfessionRegistry getInstance() {
        return INSTANCE;
    }

    private Map<String, IProfession> professions;

    public final IProfession BUILDER;

    private ProfessionRegistry() {
        this.professions = new HashMap<>();

        BUILDER = new ProfessionBuilder();
        this.registerProfession(BUILDER);
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
