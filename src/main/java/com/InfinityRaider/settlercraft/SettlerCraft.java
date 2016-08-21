package com.InfinityRaider.settlercraft;

import com.InfinityRaider.settlercraft.network.*;
import com.InfinityRaider.settlercraft.proxy.IProxy;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class SettlerCraft extends InfinityMod {
    @Mod.Instance(Reference.MOD_ID)
    public static SettlerCraft instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Override
    public IProxyBase proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }

    @Override
    public Object getModBlockRegistry() {
        return BlockRegistry.getInstance();
    }

    @Override
    public Object getModItemRegistry() {
        return ItemRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageAssignTask.class);
        wrapper.registerMessage(MessageDialogueOptionSelected.class);
        wrapper.registerMessage(MessageSettlerSleeping.class);
        wrapper.registerMessage(MessageSyncBuildingsToClient.class);
        wrapper.registerMessage(MessageSyncSettlementsToClient.class);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onPreInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onInit(FMLInitializationEvent event) {
        super.init(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onPostInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
