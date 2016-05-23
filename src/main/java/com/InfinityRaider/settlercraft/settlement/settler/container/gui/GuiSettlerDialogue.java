package com.InfinityRaider.settlercraft.settlement.settler.container.gui;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.network.MessageDialogueOptionSelected;
import com.InfinityRaider.settlercraft.network.NetWorkWrapper;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.InfinityRaider.settlercraft.utility.GuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSettlerDialogue extends GuiSettler<ContainerSettlerDialogue> {
    private static final ResourceLocation TEXTURE_TEXT_BALLOON = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/TextBalloon.png");
    private static final int TEXT_BALLOON_X = 50;
    private static final int TEXT_BALLOON_Y = 50;
    private static final int TEXT_WIDTH = 256;
    private static final int TEXT_HEIGHT = 20;
    private static final Color TEXT_COLOR = new Color(0, 0, 0);

    private List<GuiComponent<IDialogueOption>> components;
    private GuiComponent<List<String>> answer;
    private GuiComponent<List<String>> question;

    public GuiSettlerDialogue(ContainerSettlerDialogue container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        //settler answer
        List<String> answerText = getContainer().getCurrentDialogueOption().getLocalizedSettlerTextString();
        this.answer = new GuiComponent<>(answerText, this.width/2 + TEXT_BALLOON_X, TEXT_BALLOON_Y, TEXT_WIDTH, (3+answerText.size())*TEXT_HEIGHT);

        //player question
        List<String> questonText = getContainer().getCurrentDialogueOption().getLocalizedPlayerTextString();
        int offset = (3+questonText.size())*TEXT_HEIGHT;
        this.question = new GuiComponent<>(questonText, TEXT_BALLOON_X, this.height - TEXT_BALLOON_Y - offset, TEXT_WIDTH, (3+questonText.size())*TEXT_HEIGHT);

        //dialogue options
        this.components = new ArrayList<>();
        int amount = getContainer().getDialogueOptions().size();
        int x = this.width - 256 - TEXT_BALLOON_X;
        int y = this.height - TEXT_BALLOON_Y;
        for(int i = amount-1; i >= 0; i--) {
            IDialogueOption dialogueOption = getContainer().getDialogueOptions().get((i));
            int size = TEXT_HEIGHT*(1 + dialogueOption.getLocalizedPlayerTextString().size());
            y = y - size;
            components.add(new GuiComponent<>(dialogueOption, x, y, TEXT_WIDTH, size, i));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        //settler answer
        for(int i = 0; i < answer.getComponent().size(); i++) {
            this.drawCenteredString(answer.getComponent().get(i), answer.xOffset() + TEXT_WIDTH/2, answer.yOffset() + (i + 1) * TEXT_HEIGHT + 5);
        }

        //player question
        for(int i = 0; i < question.getComponent().size(); i++) {
            this.drawCenteredString(question.getComponent().get(i), question.xOffset() + TEXT_WIDTH/2, question.yOffset() + (i + 1) * TEXT_HEIGHT + 6);
        }

        //dialogue options
        for(GuiComponent<IDialogueOption> component : components) {
            List<String> textList = component.getComponent().getLocalizedPlayerTextString();
            for(int i = 0; i < textList.size(); i++) {
                this.drawCenteredString(textList.get(i), component.xOffset() + TEXT_WIDTH/2, component.yOffset() + 10 + TEXT_HEIGHT * i + 5);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_TEXT_BALLOON);

        //settler text balloon
        int size = answer.getComponent().size();
        drawTexturedModalRect(answer.xOffset(), answer.yOffset(), 0, 0, answer.xSize(), TEXT_HEIGHT);
        for(int i = 0; i < size; i++) {
            drawTexturedModalRect(answer.xOffset(), answer.yOffset() + (i + 1)*TEXT_HEIGHT, 0, TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
        }
        drawTexturedModalRect(answer.xOffset(), answer.yOffset() + (size+1)*TEXT_HEIGHT, 0, 40, TEXT_WIDTH, 2*TEXT_HEIGHT);

        //player text balloon
        size = question.getComponent().size();
        drawTexturedModalRect(question.xOffset(), question.yOffset(), 0, 0, question.xSize(), TEXT_HEIGHT);
        for(int i = 0; i < size; i++) {
            drawTexturedModalRect(question.xOffset(), question.yOffset() + (i + 1) * TEXT_HEIGHT, 0, TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
        }
        drawTexturedModalRect(question.xOffset(), question.yOffset() + (size+1)*TEXT_HEIGHT, 0, 80, TEXT_WIDTH, 2*TEXT_HEIGHT);

        //dialogue options
        for(GuiComponent<IDialogueOption> component : components) {
            int v = component.isOverComponent(mouseX, mouseY) ? 160 : 120;
            size = component.getComponent().getLocalizedPlayerTextString().size();
            drawTexturedModalRect(component.xOffset(), component.yOffset(), 0, v, component.xSize(), 10);
            for(int i = 0; i < size; i++) {
                drawTexturedModalRect(component.xOffset(), component.yOffset() + 10 + i*20, 0, v + 10, component.xSize(), 20);
            }
            drawTexturedModalRect(component.xOffset(), component.yOffset() + 10 + size*20, 0, v + 30, component.xSize(), 10);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(GuiComponent<IDialogueOption> component : components) {
            if(component.isOverComponent(mouseX, mouseY)) {
                NetWorkWrapper.getInstance().sendToServer(new MessageDialogueOptionSelected(component.getIndex()));
                getContainer().onDialogueOptionClicked(component.getIndex());
                this.initGui();
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void drawCenteredString(String text, int x, int y) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int width = fontRenderer.getStringWidth(text);
        fontRenderer.drawString(text, x - this.guiLeft - width/2, y - this.guiTop, TEXT_COLOR.getRGB(), false);
    }
}
