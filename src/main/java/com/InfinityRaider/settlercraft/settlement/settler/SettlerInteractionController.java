package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.network.MessageSettlerInteractWithEntity;
import com.InfinityRaider.settlercraft.network.MessageSettlerRightClickAir;
import com.InfinityRaider.settlercraft.network.MessageSettlerRightClickItem;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
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

/**
 * Class to simulate a player's right or left clicking with and without items for settlers
 */
public class SettlerInteractionController {
    private final EntitySettler settler;

    private BlockPos blockTarget;
    private Entity entityTarget;

    private boolean rightClickThisTick = false;
    private boolean leftClickThisTick = false;

    private int rightClickDelayTimer;

    protected SettlerInteractionController(EntitySettler settler) {
        this.settler = settler;
    }

    protected EntitySettler getSettler() {
        return settler;
    }

    protected World getWorld() {
        return getSettler().getWorld();
    }

    public void rightClick() {
        this.rightClickThisTick = true;
    }

    public void leftClick() {
        this.leftClickThisTick = true;
    }

    public void update() {
        //only run on the server thread
        if(settler.worldObj.isRemote) {
            return;
        }
        //decrement right click cool down
        if(rightClickDelayTimer > 0) {
            rightClickDelayTimer--;
        }
        //clicking & holding logic
        if (this.settler.isHandActive()) {
            if(!rightClickThisTick) {
                onStopUsingItem();
            }
        } else if(rightClickThisTick) {
            this.onRightClick();
        }
        leftClickThisTick = false;
        rightClickThisTick = false;
    }

    protected void onStopUsingItem() {

    }

    protected void onRightClick() {
        if (!isHittingBlock()) {
            this.rightClickDelayTimer = 4;
            for (EnumHand hand : EnumHand.values()) {
                ItemStack stack = getSettler().getHeldItem(hand);
                RayTraceResult target = this.raytrace();
                if (target != null) {
                    switch (target.typeOfHit) {
                        case ENTITY:
                            //handle right clicking the entity for the entity
                            Vec3d dir = target.hitVec.subtract(target.entityHit.getPositionVector());
                            new MessageSettlerInteractWithEntity(getSettler(), target.entityHit, hand, dir).sendToAll();
                            EnumActionResult resultInteract = this.interactWithEntity(target.entityHit, dir, this.getSettler().getHeldItem(hand), hand);
                            if (resultInteract == EnumActionResult.SUCCESS) {
                                return;
                            }
                            //handle right clicking the entity for the settler
                            new MessageSettlerInteractWithEntity(getSettler(), target.entityHit, hand).sendToAll();
                            if (this.interactWithEntity(target.entityHit, this.getSettler().getHeldItem(hand), hand) == EnumActionResult.SUCCESS) {
                                return;
                            }
                            break;
                        case BLOCK:
                            BlockPos pos = target.getBlockPos();
                            if (this.getWorld().getBlockState(pos).getMaterial() != Material.AIR) {
                                int stackSize = stack != null ? stack.stackSize : 0;
                                EnumActionResult result = this.processRightClickBlock(stack, pos, target.sideHit, target.hitVec, hand);
                                if (result == EnumActionResult.SUCCESS) {
                                    this.getSettler().swingArm(hand);
                                    if (stack != null) {
                                        if (stack.stackSize == 0) {
                                            this.getSettler().setHeldItem(hand, null);
                                        } else if (stack.stackSize != stackSize) {
                                            this.resetEquipProgress(hand);
                                        }
                                    }
                                    return;
                                }
                            }
                    }
                }
                ItemStack updatedStack = this.getSettler().getHeldItem(hand);
                new MessageSettlerRightClickItem(getSettler(), hand).sendToAll();
                if (updatedStack == null && (target == null || target.typeOfHit == RayTraceResult.Type.MISS)) {
                    new MessageSettlerRightClickAir(getSettler(), hand).sendToAll();
                }
                EnumActionResult result = this.processRightClick(updatedStack, hand);
                if (updatedStack != null && result == EnumActionResult.SUCCESS) {
                    this.resetEquipProgress(hand);
                    return;
                }
            }
        }
    }

    /**
     * Handles left clicking an entity (attacking)
     */
    public void attackEntity(Entity entity) {
        //TODO
    }

    /**
     * Handles right clicking an entity for the clicked entity
     */
    public EnumActionResult interactWithEntity(Entity entity, Vec3d dir, ItemStack stack, EnumHand hand) {
        //syncCurrentPlayItem();
        if(ForgeHooks.onInteractEntityAt(getSettler().getFakePlayerImplementation(), entity, dir, stack, hand)) {
            return EnumActionResult.PASS;
        }
        return entity.applyPlayerInteraction(getSettler().getFakePlayerImplementation(), dir, stack, hand);
    }

    /**
     * Handles right clicking an entity for the settler
     */
    public EnumActionResult interactWithEntity(Entity entity, ItemStack stack, EnumHand hand) {
        //syncCurrentPlayItem();
        return getSettler().interact(entity, stack, hand);
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
            if (ForgeHooks.onItemRightClick(getSettler().getFakePlayerImplementation(), hand, stack)) {
                return net.minecraft.util.EnumActionResult.PASS;
            }
            int stackSize = stack.stackSize;
            int meta = stack.getMetadata();
            ActionResult<ItemStack> result = stack.useItemRightClick(getWorld(), getSettler().getFakePlayerImplementation(), hand);
            ItemStack newStack = result.getResult();
            if (newStack == stack && newStack.stackSize == stackSize && newStack.getMaxItemUseDuration() <= 0 && newStack.getMetadata() == meta) {
                return result.getType();
            } else {
                getSettler().setHeldItem(hand, newStack);
                if (newStack.stackSize <= 0) {
                    getSettler().setHeldItem(hand, null);
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
