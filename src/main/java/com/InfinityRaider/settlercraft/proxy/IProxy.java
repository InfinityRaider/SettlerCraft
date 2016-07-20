package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.infinityraider.infinitylib.proxy.IProxyBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public interface IProxy extends IProxyBase{
    /**
     * @return The settlement handler relevant to the effective side
     */
    ISettlementHandler getSettlementHandler();
}
