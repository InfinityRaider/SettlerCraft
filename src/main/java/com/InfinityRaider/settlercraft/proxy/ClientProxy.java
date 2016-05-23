package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.registry.IconRegistry;
import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.InfinityRaider.settlercraft.render.RenderSettlement;
import com.InfinityRaider.settlercraft.render.entity.RenderSettler;
import com.InfinityRaider.settlercraft.render.schematic.SchematicInWorldPlannerRenderer;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntitySettler.class, RenderSettler.getFacotry());
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public World getWorldByDimensionId(int dimension) {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if(effectiveSide == Side.SERVER) {
            return FMLClientHandler.instance().getServer().worldServerForDimension(dimension);
        } else {
            return getClientWorld();
        }
    }

    @Override
    public SettlementHandler getSettlementHandler() {
        return getEffectiveSide() == Side.CLIENT ? SettlementHandler.getClientInstance() : SettlementHandler.getServerInstance();
    }

    @Override
    public Side getPhysicalSide() {
        return Side.CLIENT;
    }

    @Override
    public Side getEffectiveSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(SchematicInWorldPlannerRenderer.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderSettlement.getInstance());
        MinecraftForge.EVENT_BUS.register(SettlementHandler.getClientInstance());
        MinecraftForge.EVENT_BUS.register(SettlementHandler.getServerInstance());
        MinecraftForge.EVENT_BUS.register(IconRegistry.getInstance());
    }

    @Override
    public void registerRenderers() {
        //items
        ItemRegistry.getInstance().registerRenderers();
        //blocks
        BlockRegistry.getInstance().registerRenderers();
    }

    @Override
    public void queueTask(Runnable task) {
        if(getEffectiveSide() == Side.CLIENT) {
            Minecraft.getMinecraft().addScheduledTask(task);
        } else {
            FMLClientHandler.instance().getServer().addScheduledTask(task);
        }
    }
}
