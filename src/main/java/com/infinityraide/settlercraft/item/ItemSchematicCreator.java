package com.infinityraide.settlercraft.item;

import com.infinityraide.settlercraft.reference.Reference;
import com.infinityraide.settlercraft.utility.schematic.SchematicWriter;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemSchematicCreator extends ItemWithModelBase {
    public ItemSchematicCreator() {
        super("schematicCreator");
        this.setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            SchematicWriter.getInstance().onBlockClicked(world, pos);
        }
        return EnumActionResult.PASS;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TranslationHelper.translateToLocal(Reference.MOD_ID.toLowerCase() + ".tooltip_schematicCreator_L1"));
        tooltip.add(TranslationHelper.translateToLocal(Reference.MOD_ID.toLowerCase()+".tooltip_schematicCreator_L2"));
        tooltip.add(TranslationHelper.translateToLocal(Reference.MOD_ID.toLowerCase()+".tooltip_schematicCreator_L3"));
        tooltip.add(TranslationHelper.translateToLocal(Reference.MOD_ID.toLowerCase()+".tooltip_schematicCreator_L4"));
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase()+ ":" + getInternalName(), "inventory")));
        return list;
    }
}
