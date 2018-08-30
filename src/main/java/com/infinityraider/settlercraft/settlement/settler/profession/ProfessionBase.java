package com.infinityraider.settlercraft.settlement.settler.profession;

import com.infinityraider.settlercraft.api.v1.IProfession;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.reference.Reference;
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
