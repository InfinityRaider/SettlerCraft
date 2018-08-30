package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerInteractWithEntity extends MessageBase<IMessage> {
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
        if(settler != null && target != null) {
            if(dir != null) {
                ItemStack stack = settler.getHeldItem(hand);
                settler.getInteractionController().interactWithEntity(target, dir, stack, hand);
            } else if(hand != null) {
                settler.getInteractionController().interactWithEntity(target, hand);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
