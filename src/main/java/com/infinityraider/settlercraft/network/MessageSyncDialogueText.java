package com.infinityraider.settlercraft.network;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.infinityraider.settlercraft.settlement.settler.container.gui.GuiSettlerDialogue;
import com.google.common.collect.Lists;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.stream.Collectors;

public class MessageSyncDialogueText extends MessageBase<IMessage> {
    private ITextComponent[] question;
    private ITextComponent[] answer;
    private ITextComponent[][] options;

    public MessageSyncDialogueText() {
        super();
    }

    public MessageSyncDialogueText(ContainerSettlerDialogue dialogue) {
        this();
        List<ITextComponent> questionText = dialogue.getCurrentDialogueOption().getSettlerText();
        this.question = questionText.toArray(new ITextComponent[questionText.size()]);
        List<ITextComponent> answerText = dialogue.getCurrentDialogueOption().getSettlerText();
        this.answer = answerText.toArray(new ITextComponent[answerText.size()]);
        List<List<ITextComponent>> optionsText = dialogue.getDialogueOptions().stream().map(IDialogueOption::getPlayerText).collect(Collectors.toList());
        this.options = new ITextComponent[optionsText.size()][];
        for(int i = 0; i < optionsText.size(); i++) {
            List<ITextComponent> optionText = optionsText.get(i);
            this.options[i] = optionText.toArray(new ITextComponent[optionText.size()]);
        }
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
        List<ITextComponent> questionText = Lists.newArrayList(this.question);
        List<ITextComponent> answerText = Lists.newArrayList(this.answer);
        List<List<ITextComponent>> optionsText = Lists.newArrayList();
        for(ITextComponent[] optionText : this.options) {
            optionsText.add(Lists.newArrayList(optionText));
        }
        if (gui == null) {
            //if the container has just been opened, this messages reaches the client before the gui is opened on the client
            GuiSettlerDialogue.cachedQuestion = questionText;
            GuiSettlerDialogue.cachedAnswer = answerText;
            GuiSettlerDialogue.cachedOptions = optionsText;
        } else if (gui instanceof GuiSettlerDialogue) {
            //after the gui has been opened and the dialogue options are updated, this works without problems
            ((GuiSettlerDialogue) gui).updateDialogueOptions(questionText, answerText, optionsText);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
