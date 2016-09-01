package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerInteractWithEntity extends MessageBaseSettler<IMessage> {
    private EntitySettler settler;
    private Entity target;
    private EnumHand hand;
    private Vec3d dir;

    public MessageSettlerInteractWithEntity() {
        super();
    }

    public MessageSettlerInteractWithEntity(EntitySettler settler, Entity target) {
        this();
        this.settler = settler;
        this.target = target;
    }

    public MessageSettlerInteractWithEntity(EntitySettler settler, Entity target, EnumHand hand) {
        this(settler, target);
        this.hand = hand;
    }

    public MessageSettlerInteractWithEntity(EntitySettler settler, Entity target, EnumHand hand, Vec3d dir) {
        this(settler, target, hand);
        this.dir = dir;
    }


    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && settler != null && target != null) {
            if(dir != null) {
                ItemStack stack = settler.getHeldItem(hand);
                settler.getInteractionController().interactWithEntity(target, dir, stack, hand);
            } else if(hand != null) {
                ItemStack stack = settler.getHeldItem(hand);
                settler.getInteractionController().interactWithEntity(target, stack, hand);
            } else {
                settler.getInteractionController().attackEntity(target);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.settler = this.readSettlerFromByteBuf(buf);
        this.target = this.readEntityFromByteBuf(buf);
        if(buf.readBoolean()) {
            this.hand = EnumHand.values()[buf.readInt()];
        }
        if(buf.readBoolean()) {
            this.dir = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeSettlerToByteBuf(buf, settler);
        this.writeEntityToByteBuf(buf, target);
        buf.writeBoolean(hand != null);
        if(hand != null) {
            buf.writeInt(hand.ordinal());
        }
        buf.writeBoolean(dir != null);
        if(dir != null) {
            buf.writeDouble(dir.xCoord);
            buf.writeDouble(dir.yCoord);
            buf.writeDouble(dir.zCoord);
        }
    }
}
