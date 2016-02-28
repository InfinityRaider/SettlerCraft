package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.IProfession;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ProfessionBase implements IProfession {
    private final String name;

    protected ProfessionBase(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getEntityTexture(ISettler settler) {
        String appendix = (settler.isMale() ? "_male" : "_female");
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/settler/" + getName() + appendix +".png");
    }
}
