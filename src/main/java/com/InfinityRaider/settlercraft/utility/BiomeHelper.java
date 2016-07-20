package com.InfinityRaider.settlercraft.utility;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeHelper {
    private static final BiomeHelper INSTANCE = new BiomeHelper();

    public static BiomeHelper getInstance() {
        return INSTANCE;
    }

    private BiomeHelper() {}

    public String[] getBiomeList() {
        List<String> list = Biome.EXPLORATION_BIOMES_LIST.stream().map(Biome::getBiomeName).collect(Collectors.toList());
        return list.toArray(new String[list.size()]);
    }

    public Biome[] convertBiomeNamesList(String[] names) {
        List<Biome> list = new ArrayList<>();
        for(String name : names) {
            Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(name));
            if(biome != null) {
                list.add(biome);
            }
        }
        return list.toArray(new Biome[list.size()]);
    }
}
