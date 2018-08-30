package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageDialogueOptionSelected extends MessageBase<IMessage> {
    private int id;

    public MessageDialogueOptionSelected() {
        super();
    }

    public MessageDialogueOptionSelected(int id) {
        this();
        this.id = id;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        Container container = player.openContainer;
        if (container instanceof ContainerSettlerDialogue) {
            ((ContainerSettlerDialogue) container).onDialogueOptionClicked(this.id);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
