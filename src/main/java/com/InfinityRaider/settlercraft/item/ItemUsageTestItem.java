package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.utility.InfinityLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class ItemUsageTestItem extends ItemBase {
    public static final InfinityLogger LOGGER = SettlerCraft.instance.getLogger();

    public ItemUsageTestItem() {
        super("use_test_item");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onItemUse");
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onItemRightClick");
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        LOGGER.debug((entity.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onItemUseFinish");
        return stack;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        LOGGER.debug((target.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " hitEntity");
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " itemInteractionForEntity");
        return false;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        LOGGER.debug((SettlerCraft.instance.getEffectiveSide() == Side.CLIENT ? "CLIENT:" : "SERVER:") + " getMaxItemUseDuration");
        return 0;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        LOGGER.debug((entity.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onPlayerStoppedUsing");
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
                                           EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onItemUseFirst");
        return EnumActionResult.PASS;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onBlockStartBreak");
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onUsingTick");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        LOGGER.debug((player.worldObj.isRemote ? "CLIENT:" : "SERVER:") + " onLeftClickEntity");
        return false;
    }

}
