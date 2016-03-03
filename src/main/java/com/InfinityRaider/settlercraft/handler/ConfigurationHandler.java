package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.utility.BiomeHelper;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigurationHandler {
    private static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

    private ConfigurationHandler() {}

    public static ConfigurationHandler getInstance() {
        return INSTANCE;
    }

    private Configuration config;

    //settlers
    public String[] settlerSpawnBiomes;
    public int settlerSpawnWeight;

    //debug
    public boolean debug;

    //client
    @SideOnly(Side.CLIENT)
    public String schematicOutput;

    public void init(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadConfiguration();
        if(config.hasChanged()) {
            config.save();
        }
        LogHelper.debug("Configuration Loaded");
    }

    @SideOnly(Side.CLIENT)
    public void initClientConfigs(FMLPreInitializationEvent event) {
        if(config == null) {
            config = new Configuration(event.getSuggestedConfigurationFile());
        }
        loadClientConfiguration(event);
        if(config.hasChanged()) {
            config.save();
        }
        LogHelper.debug("Client configuration Loaded");
    }

    private void loadConfiguration() {
        //settlers
        settlerSpawnBiomes = config.getStringList("settler spawn biomes", Categories.SETTLERS.getName(), BiomeHelper.getInstance().getBiomeList(), "Biomes where settlers can spawn");
        settlerSpawnWeight = config.getInt("settler spawn weight", Categories.SETTLERS.getName(), 1, 1, 20, "The spawn weight for spawning settlers in the world");
        //debug
        debug = config.getBoolean("debug", Categories.DEBUG.getName(), false, "Set to true if you wish to enable debug mode");
    }

    @SideOnly(Side.CLIENT)
    private void loadClientConfiguration(FMLPreInitializationEvent event) {
        schematicOutput = config.getString("Schematic output file", Categories.CLIENT.getName(), event.getModConfigurationDirectory().getAbsolutePath() + "\\schematics\\schematic.json", "The location to the file where schematics will be saved");
    }

    public enum Categories {
        SETTLERS("settlers"),
        DEBUG("debug"),
        CLIENT("client");

        private final String name;

        Categories(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
