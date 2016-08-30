package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.InfinityRaider.settlercraft.settlement.settler.container.gui.GuiSettlerDialogue;
import com.infinityraider.infinitylib.network.MessageBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageSyncDialogueText extends MessageBase<IMessage> {
    private List<ITextComponent> question;
    private List<ITextComponent> answer;
    private List<List<ITextComponent>> options;

    public MessageSyncDialogueText() {
        super();
    }

    public MessageSyncDialogueText(ContainerSettlerDialogue dialogue) {
        this();
        this.question = dialogue.getCurrentDialogueOption().getSettlerText();
        this.answer = dialogue.getCurrentDialogueOption().getPlayerText();
        this.options = new ArrayList<>();
        this.options.addAll(dialogue.getDialogueOptions().stream().map(IDialogueOption::getPlayerText).collect(Collectors.toList()));
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            GuiScreen gui = Minecraft.getMinecraft().currentScreen;
            if(gui == null) {
                //if the container has just been opened, this messages reaches the client before the gui is opened on the client
                GuiSettlerDialogue.cachedQuestion = question;
                GuiSettlerDialogue.cachedAnswer = answer;
                GuiSettlerDialogue.cachedOptions = options;
            }
            else if(gui instanceof GuiSettlerDialogue) {
                //after the gui has been opened and the dialogue options are updated, this works without problems
                ((GuiSettlerDialogue) gui).updateDialogueOptions(question, answer, options);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.question = readTextComponentListFromBuffer(buf);
        this.answer = readTextComponentListFromBuffer(buf);
        int size = buf.readInt();
        this.options = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            this.options.add(this.readTextComponentListFromBuffer(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeTextComponentListToBuffer(this.question, buf);
        this.writeTextComponentListToBuffer(this.answer, buf);
        buf.writeInt(this.options.size());
        for (List<ITextComponent> option : this.options) {
            this.writeTextComponentListToBuffer(option, buf);
        }
    }

    private void writeTextComponentListToBuffer(List<ITextComponent> list, ByteBuf buf) {
        buf.writeInt(list.size());
        for(ITextComponent text : list) {
            String serialized = ITextComponent.Serializer.componentToJson(text);
            this.writeStringToByteBuf(buf, serialized);
        }
    }

    private List<ITextComponent> readTextComponentListFromBuffer(ByteBuf buf) {
        List<ITextComponent> list = new ArrayList<>();
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            String serialized = this.readStringFromByteBuf(buf);
            list.add(ITextComponent.Serializer.jsonToComponent(serialized));
        }
        return list;
    }
}
