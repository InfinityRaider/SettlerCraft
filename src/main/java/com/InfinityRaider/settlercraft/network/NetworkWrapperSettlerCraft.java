package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkWrapperSettlerCraft {
    private static NetworkWrapperSettlerCraft INSTANCE = new NetworkWrapperSettlerCraft();

    public static NetworkWrapperSettlerCraft getInstance() {
        return INSTANCE;
    }

    private final SimpleNetworkWrapper wrapper;
    private int nextId = 0;

    private NetworkWrapperSettlerCraft() {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID.toLowerCase());}

    public void init() {
        registerMessage(MessageDialogueOptionSelected.MessageHandler.class, MessageDialogueOptionSelected.class);
    }

    public void sendToAll(MessageBase message) {
        wrapper.sendToAll(message);
    }

    public void sendTo(MessageBase message, EntityPlayerMP player) {
        wrapper.sendTo(message, player);
    }

    public void sendToAllAround(MessageBase message, World world, double x, double y, double z, double range) {
        sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimensionId(), x, y, z, range));
    }

    public void sendToAllAround(MessageBase message, int dimension, double x, double y, double z, double range) {
        sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public void sendToAllAround(MessageBase message, NetworkRegistry.TargetPoint point) {
        wrapper.sendToAllAround(message, point);
    }

    public void sendToDimension(MessageBase messageBase, World world) {
        sendToDimension(messageBase, world.provider.getDimensionId());
    }

    public void sendToDimension(MessageBase message, int dimensionId) {
        wrapper.sendToDimension(message, dimensionId);
    }

    public void sendToServer(MessageBase message) {
        wrapper.sendToServer(message);
    }

    private <REQ extends MessageBase, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> message) {
        try {
            Side side = message.getDeclaredConstructor().newInstance().getMessageHandlerSide();
            wrapper.registerMessage(messageHandler, message, nextId, side);
            nextId = nextId + 1;
        } catch (Exception e) {
            LogHelper.printStackTrace(e);
        }
    }
}
