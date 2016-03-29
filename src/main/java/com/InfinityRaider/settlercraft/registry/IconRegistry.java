package com.InfinityRaider.settlercraft.registry;

import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconRegistry {
    private static final IconRegistry INSTANCE = new IconRegistry();

    public static IconRegistry getInstance() {
        return INSTANCE;
    }

    private IconRegistry() {}

    public TextureAtlasSprite icon_mainHandBackground;

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void initializeIcons(TextureStitchEvent event) {
        icon_mainHandBackground = event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "gui/mainSlot_backGround"));
    }
}
