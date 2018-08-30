package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerDigging extends MessageBase<IMessage> {
    private EntitySettler settler;
    private CPacketPlayerDigging.Action action;
    private EnumFacing face;
    private BlockPos pos;

    public MessageSettlerDigging() {
        super();
    }

    public MessageSettlerDigging(EntitySettler settler, CPacketPlayerDigging.Action action, EnumFacing face, BlockPos pos) {
        this();
        this.settler = settler;
        this.action = action;
        this.face = face;
        this.pos = pos;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        //TODO
        /*
        double x = this.settler.posX - ((double) this.pos.getX() + 0.5D);
        double y = this.settler.posY - ((double) this.pos.getY() + 0.5D) + 1.5D;
        double z = this.settler.posZ - ((double) this.pos.getZ() + 0.5D);
        double d = x * x + y * y + z * z;

        double dist = settler.interactionManager.getBlockReachDistance() + 1;
        dist *= dist;

        if (d <= dist && this.pos.getY() < this.serverController.getBuildLimit()) {
            if (this.action == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                if (!this.serverController.isBlockProtected(worldserver, blockpos, this.playerEntity) && worldserver.getWorldBorder().contains(blockpos)) {
                    this.playerEntity.interactionManager.onBlockClicked(blockpos, packetIn.getFacing());
                } else {
                    this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                }
            } else {
                if (this.action == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    this.playerEntity.interactionManager.blockRemoving(blockpos);
                } else if (packetIn.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                    this.playerEntity.interactionManager.cancelDestroyingBlock();
                }
                if (worldserver.getBlockState(blockpos).getMaterial() != Material.AIR)
                {
                    this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                }
            }

            return;
        }
        */
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
