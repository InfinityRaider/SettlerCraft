package com.InfinityRaider.settlercraft;

import com.InfinityRaider.settlercraft.network.*;
import com.InfinityRaider.settlercraft.proxy.IProxy;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.registry.EntityRegistry;
import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.VERSION,
        dependencies = "required-after:infinitylib"
)
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
    public Object getModBiomeRegistry() {
        return this;
    }

    @Override
    public Object getModEnchantmentRegistry() {
        return this;
    }

    @Override
    public Object getModEntityRegistry() {
        return EntityRegistry.getInstance();
    }

    @Override
    public Object getModPotionRegistry() {
        return this;
    }

    @Override
    public Object getModPotionTypeRegistry() {
        return this;
    }

    @Override
    public Object getModSoundRegistry() {
        return this;
    }

    @Override
    public Object getModVillagerProfessionRegistry() {
        return this;
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageCloseContainer.class);
        wrapper.registerMessage(MessageDialogueOptionSelected.class);
        wrapper.registerMessage(MessageSettlerInteractWithEntity.class);
        wrapper.registerMessage(MessageSettlerLeftClickEntity.class);
        wrapper.registerMessage(MessageSettlerRightClickAir.class);
        wrapper.registerMessage(MessageSettlerRightClickItem.class);
        wrapper.registerMessage(MessageSettlerSleeping.class);
        wrapper.registerMessage(MessageSyncBuildingsToClient.class);
        wrapper.registerMessage(MessageSyncDialogueText.class);
        wrapper.registerMessage(MessageSyncSettlementsToClient.class);
    }
}
