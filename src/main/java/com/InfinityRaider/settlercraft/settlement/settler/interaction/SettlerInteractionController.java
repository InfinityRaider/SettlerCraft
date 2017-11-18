package com.InfinityRaider.settlercraft.settlement.settler.interaction;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.network.*;
import com.InfinityRaider.settlercraft.settlement.settler.EntityPlayerWrappedSettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Class to simulate a player's right or left clicking with and without items for settlers
 */
public class SettlerInteractionController {
    private final EntitySettler settler;

    private SettlerInteraction currentAction;
    private int rightClickDelayTimer;

    private boolean isHittingBlock;
    private ItemStack currentItemHittingBlock;
    private float curBlockDamageMP;
    private BlockPos currentBlock = new BlockPos(-1, -1, -1);

    public SettlerInteractionController(EntitySettler settler) {
        this.settler = settler;
    }

    public void interactWithItem(EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        this.setInteraction(SettlerInteraction.interactWithItem(this, hand, leftClick, sneak, usageTicks));
    }

    public void interactWithBlock(BlockPos target, EnumFacing side, Vec3d hit, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        this.setInteraction(SettlerInteraction.interactWitBlock(this, target, side, hit, hand, leftClick, sneak, usageTicks));
    }

    public void interactWithEntity(Entity target, Vec3d hit, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        this.setInteraction(SettlerInteraction.interactWithEntity(this, target, hit, hand, leftClick, sneak, usageTicks));
    }

    protected void setInteraction(SettlerInteraction action) {
        if(!action.isLeftClick() && this.rightClickDelayTimer > 0) {
            return;
        }
        this.currentAction = action;
    }

    protected EntitySettler getSettler() {
        return settler;
    }

    protected EntityPlayerWrappedSettler getFakePlayer() {
        return this.getSettler().getFakePlayerImplementation();
    }

    protected World getWorld() {
        return getSettler().getWorld();
    }

    public void update() {
        //only run on the server thread
        if(this.getSettler().getEntityWorld().isRemote || this.currentAction == null) {
            return;
        }
        if(!this.currentAction.isLeftClick()) {
            this.rightClickDelayTimer = 4;
        }
        if(this.currentAction.update()) {
            this.currentAction = null;
        }
        //decrement right click cool down
        if(rightClickDelayTimer > 0) {
            rightClickDelayTimer--;
        }
    }

    public void cancelInteraction() {
        if(this.getSettler().getEntityWorld().isRemote || this.currentAction == null) {
            return;
        }
        if(!this.currentAction.isLeftClick()) {
            this.onStopUsingItem();
        }
        this.currentAction = null;
    }

    protected void onStopUsingItem() {

    }

    public boolean leftClickAir() {
        //TODO;
        return false;
    }

    public boolean rightClickAir(EnumHand hand) {
        this.afterRightClick(hand, true);
        return false;
    }

    public boolean leftClickEntity(Entity target) {
        new MessageSettlerLeftClickEntity(this.getSettler(), target).sendToAll();
        getSettler().attackTargetEntityWithCurrentItem(target);
        getSettler().resetCooldown();
        return false;
    }

    public boolean rightClickEntity(Entity target, EnumHand hand, Vec3d hit) {
        //handle right clicking the entity for the entity
        new MessageSettlerInteractWithEntity(getSettler(), target, hand, hit).sendToAll();
        EnumActionResult resultInteract = this.interactWithEntity(target, hit, this.getSettler().getHeldItem(hand), hand);
        if (resultInteract == EnumActionResult.SUCCESS) {
            return true;
        }
        //handle right clicking the entity for the settler
        new MessageSettlerInteractWithEntity(getSettler(), target, hand).sendToAll();
        if (this.interactWithEntity(target, hand) == EnumActionResult.SUCCESS) {
            return true;
        }
        this.afterRightClick(hand, false);
        return false;
    }

    public boolean leftClickBlock(BlockPos pos, EnumFacing side, Vec3d hit) {
        if (!this.getSettler().getWorld().getWorldBorder().contains(pos)
                || SettlerCraft.instance.getMinecraftServer().isBlockProtected(this.getWorld(), pos, this.getFakePlayer())) {
            return true;
        } else {
            if (!this.isHittingBlock || !this.isHittingPosition(pos)) {
                if (this.isHittingBlock) {
                    new MessageSettlerDigging(this.getSettler(), CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, side, this.currentBlock).sendToAll();
                }
                new MessageSettlerDigging(this.getSettler(), CPacketPlayerDigging.Action.START_DESTROY_BLOCK, side, pos).sendToAll();
                PlayerInteractEvent.LeftClickBlock event = ForgeHooks.onLeftClickBlock(this.getFakePlayer(), pos, side, hit);
                IBlockState state = this.getWorld().getBlockState(pos);
                boolean flag = state.getMaterial() != Material.AIR;
                if (flag && this.curBlockDamageMP == 0.0F) {
                    if (event.getUseBlock() != Event.Result.DENY)
                        state.getBlock().onBlockClicked(this.getWorld(), pos, this.getFakePlayer());
                }
                if (event.getUseItem() == Event.Result.DENY) return true;
                if (flag && state.getPlayerRelativeBlockHardness(this.getFakePlayer(), this.getWorld(), pos) >= 1.0F) {
                    this.onPlayerDestroyBlock();
                } else {
                    this.isHittingBlock = true;
                    this.currentBlock = pos;
                    this.currentItemHittingBlock = this.getSettler().getHeldItemMainhand();
                    this.curBlockDamageMP = 0.0F;
                    //this.stepSoundTickCounter = 0.0F;
                    this.getWorld().sendBlockBreakProgress(this.getSettler().getEntityId(), this.currentBlock, (int)(this.curBlockDamageMP * 10.0F) - 1);
                }
            }
            return true;
        }
    }

    private void onPlayerDestroyBlock() {}

    private boolean isHittingPosition(BlockPos pos) {
        ItemStack itemstack = this.getSettler().getHeldItemMainhand();
        boolean flag = this.currentItemHittingBlock == null && itemstack == null;
        if (this.currentItemHittingBlock != null && itemstack != null) {
            flag = !net.minecraftforge.client.ForgeHooksClient.shouldCauseBlockBreakReset(this.currentItemHittingBlock, itemstack);
        }
        return pos.equals(this.currentBlock) && flag;
    }

    public boolean rightClickBlock(BlockPos pos, EnumFacing side, Vec3d hit, EnumHand hand) {
        ItemStack stack = getSettler().getHeldItem(hand);
        if (this.getWorld().getBlockState(pos).getMaterial() != Material.AIR) {
            int stackSize = stack.getCount();
            EnumActionResult result = this.processRightClickBlock(stack, pos, side, hit, hand);
            if (result == EnumActionResult.SUCCESS) {
                this.getSettler().swingArm(hand);
                if(stack.isEmpty()) {
                    this.getSettler().setHeldItem(hand, ItemStack.EMPTY);
                } else if(stack.getCount() != stackSize) {
                    this.resetEquipProgress(hand);
                }
                return true;
            }
        }
        this.afterRightClick(hand, false);
        return false;
    }

    private void afterRightClick(EnumHand hand, boolean miss) {
        ItemStack stack = this.getSettler().getHeldItem(hand);
        new MessageSettlerRightClickItem(getSettler(), hand).sendToAll();
        if (stack == null && miss) {
            new MessageSettlerRightClickAir(getSettler(), hand).sendToAll();
        }
        EnumActionResult result = this.processRightClick(stack, hand);
        if (stack != null && result == EnumActionResult.SUCCESS) {
            this.resetEquipProgress(hand);
        }
    }

    /**
     * Handles right clicking an entity for the clicked entity
     */
    public EnumActionResult interactWithEntity(Entity entity, Vec3d dir, ItemStack stack, EnumHand hand) {
        //syncCurrentPlayItem();
        return ForgeHooks.onInteractEntityAt(getSettler().getFakePlayerImplementation(), entity, dir, hand);
    }

    /**
     * Handles right clicking an entity for the settler
     */
    public EnumActionResult interactWithEntity(Entity entity, EnumHand hand) {
        //syncCurrentPlayItem();
        return getSettler().interact(entity, hand);
    }

    /**
     * Handles right clicking on a block
     */
    public EnumActionResult processRightClickBlock(ItemStack stack, BlockPos pos, EnumFacing side, Vec3d hitVec, EnumHand hand) {
        //TODO
        return EnumActionResult.FAIL;
    }

    /**
     * Handles right clicking of an item
     */
    public EnumActionResult processRightClick(ItemStack stack, EnumHand hand) {
        if (getSettler().getCooldownTracker().hasCooldown(stack.getItem())) {
            return EnumActionResult.PASS;
        } else {
            EnumActionResult eventResult = ForgeHooks.onItemRightClick(getSettler().getFakePlayerImplementation(), hand);
            if (eventResult != null) {
                return eventResult;
            }
            int stackSize = stack.getCount();
            int meta = stack.getMetadata();
            ActionResult<ItemStack> result = stack.useItemRightClick(getWorld(), getSettler().getFakePlayerImplementation(), hand);
            ItemStack newStack = result.getResult();
            if (newStack == stack && newStack.getCount() == stackSize && newStack.getMaxItemUseDuration() <= 0 && newStack.getMetadata() == meta) {
                return result.getType();
            } else {
                getSettler().setHeldItem(hand, newStack);
                if (newStack.isEmpty()) {
                    getSettler().setHeldItem(hand, ItemStack.EMPTY);
                    ForgeEventFactory.onPlayerDestroyItem(getSettler().getFakePlayerImplementation(), newStack, hand);
                }
            }
            return result.getType();
        }
    }

    protected RayTraceResult raytrace() {
        return RayTraceHelper.getTargetEntityOrBlock(getSettler(), getSettler().getInteractionRangeAttribute().getAttributeValue());
    }

    protected boolean isHittingBlock() {
        //TODO
        return false;
    }

    protected void resetEquipProgress(EnumHand hand) {
        //TODO
    }

}
