package com.InfinityRaider.settlercraft.settlement.settler.container.gui;

import com.InfinityRaider.settlercraft.network.MessageDialogueOptionSelected;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.infinityraider.infinitylib.utility.GuiComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSettlerDialogue extends GuiSettler<ContainerSettlerDialogue> {
    public static List<List<ITextComponent>> cachedOptions;
    public static List<ITextComponent> cachedAnswer;
    public static List<ITextComponent> cachedQuestion;

    private static final ResourceLocation TEXTURE_TEXT_BALLOON = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/TextBalloon.png");
    private static final int TEXT_BALLOON_X = 50;
    private static final int TEXT_BALLOON_Y = 50;
    private static final int TEXT_WIDTH = 256;
    private static final int TEXT_HEIGHT = 20;
    private static final Color TEXT_COLOR = new Color(0, 0, 0);

    private List<GuiComponent<List<ITextComponent>>> options;
    private GuiComponent<List<ITextComponent>> answer;
    private GuiComponent<List<ITextComponent>> question;

    public GuiSettlerDialogue(ContainerSettlerDialogue container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.updateOptionsFromCache();
    }

    private void updateOptionsFromCache() {
        if(cachedOptions != null && cachedAnswer != null && cachedQuestion != null) {
            this.updateDialogueOptions(cachedQuestion, cachedAnswer, cachedOptions);
        }
        cachedOptions = null;
        cachedAnswer = null;
        cachedQuestion = null;
    }

    public void updateDialogueOptions(List<ITextComponent> question, List<ITextComponent> answer, List<List<ITextComponent>> options) {
        //settler answer
        this.answer = new GuiComponent<>(answer, this.width/2 + TEXT_BALLOON_X, TEXT_BALLOON_Y, TEXT_WIDTH, (3 + answer.size())*TEXT_HEIGHT);

        //player question
        int offset = (3 + question.size())*TEXT_HEIGHT;
        this.question = new GuiComponent<>(question, TEXT_BALLOON_X, this.height - TEXT_BALLOON_Y - offset, TEXT_WIDTH, (3 + question.size())*TEXT_HEIGHT);

        //dialogue options
        this.options = new ArrayList<>();
        int amount = options.size();
        int x = this.width - 256 - TEXT_BALLOON_X;
        int y = this.height - TEXT_BALLOON_Y;
        for(int i = amount - 1; i >= 0; i--) {
            List<ITextComponent> dialogueOption = options.get((i));
            int size = TEXT_HEIGHT*(1 + dialogueOption.size());
            y = y - size;
            this.options.add(new GuiComponent<>(dialogueOption, x, y, TEXT_WIDTH, size, i));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        //settler answer
        if(this.answer != null) {
            for (int i = 0; i < answer.getComponent().size(); i++) {
                this.drawCenteredString(answer.getComponent().get(i), answer.xOffset() + TEXT_WIDTH / 2, answer.yOffset() + (i + 1) * TEXT_HEIGHT + 5);
            }
        }

        //player question
        if(this.question != null) {
            for (int i = 0; i < question.getComponent().size(); i++) {
                this.drawCenteredString(question.getComponent().get(i), question.xOffset() + TEXT_WIDTH / 2, question.yOffset() + (i + 1) * TEXT_HEIGHT + 6);
            }
        }

        //dialogue options
        if(this.options != null) {
            for (GuiComponent<List<ITextComponent>> component : options) {
                List<ITextComponent> textList = component.getComponent();
                for (int i = 0; i < textList.size(); i++) {
                    this.drawCenteredString(textList.get(i), component.xOffset() + TEXT_WIDTH / 2, component.yOffset() + 10 + TEXT_HEIGHT * i + 5);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_TEXT_BALLOON);

        //settler text balloon
        if(this.answer != null) {
            int size = answer.getComponent().size();
            drawTexturedModalRect(answer.xOffset(), answer.yOffset(), 0, 0, answer.xSize(), TEXT_HEIGHT);
            for (int i = 0; i < size; i++) {
                drawTexturedModalRect(answer.xOffset(), answer.yOffset() + (i + 1) * TEXT_HEIGHT, 0, TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
            }
            drawTexturedModalRect(answer.xOffset(), answer.yOffset() + (size + 1) * TEXT_HEIGHT, 0, 40, TEXT_WIDTH, 2 * TEXT_HEIGHT);
        }

        //player text balloon
        if(this.question != null) {
            int size = question.getComponent().size();
            drawTexturedModalRect(question.xOffset(), question.yOffset(), 0, 0, question.xSize(), TEXT_HEIGHT);
            for (int i = 0; i < size; i++) {
                drawTexturedModalRect(question.xOffset(), question.yOffset() + (i + 1) * TEXT_HEIGHT, 0, TEXT_HEIGHT, TEXT_WIDTH, TEXT_HEIGHT);
            }
            drawTexturedModalRect(question.xOffset(), question.yOffset() + (size + 1) * TEXT_HEIGHT, 0, 80, TEXT_WIDTH, 2 * TEXT_HEIGHT);
        }

        //dialogue options
        if(this.options != null) {
            for (GuiComponent<List<ITextComponent>> component : options) {
                int v = component.isOverComponent(mouseX, mouseY) ? 160 : 120;
                int size = component.getComponent().size();
                drawTexturedModalRect(component.xOffset(), component.yOffset(), 0, v, component.xSize(), 10);
                for (int i = 0; i < size; i++) {
                    drawTexturedModalRect(component.xOffset(), component.yOffset() + 10 + i * 20, 0, v + 10, component.xSize(), 20);
                }
                drawTexturedModalRect(component.xOffset(), component.yOffset() + 10 + size * 20, 0, v + 30, component.xSize(), 10);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(this.options != null) {
            for (GuiComponent<List<ITextComponent>> component : options) {
                if (component.isOverComponent(mouseX, mouseY)) {
                    NetworkWrapper.getInstance().sendToServer(new MessageDialogueOptionSelected(component.getIndex()));
                    this.initGui();
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        cachedQuestion = null;
        cachedAnswer = null;
        cachedOptions = null;
        super.onGuiClosed();
    }

    private void drawCenteredString(ITextComponent text, int x, int y) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        String formattedText = text.getFormattedText();
        int width = fontRenderer.getStringWidth(formattedText);
        fontRenderer.drawString(formattedText, x - this.guiLeft - width/2, y - this.guiTop, TEXT_COLOR.getRGB(), false);
    }
}
