package com.infinityraide.settlercraft.settlement.settler.interaction;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class SettlerInteraction {
    private final SettlerInteractionController controller;

    private final EnumHand hand;
    private final boolean leftClick;
    private final boolean sneak;
    private final int usageTicks;

    private int currentTick;

    private SettlerInteraction(SettlerInteractionController controller, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        this.controller = controller;
        this.hand = hand;
        this.leftClick = leftClick;
        this.sneak = sneak;
        this.usageTicks = usageTicks;
        this.currentTick = 0;
    }

    public SettlerInteractionController getController() {
        return this.controller;
    }

    public EntitySettler getSettler() {
        return this.getController().getSettler();
    }

    public boolean isLeftClick() {
        return this.leftClick;
    }

    public boolean isSneak() {
        return this.sneak;
    }

    public EnumHand getHand() {
        return this.hand;
    }

    public int getMaxUsageTicks() {
        return this.usageTicks;
    }

    public int getUsageTicks() {
        return this.currentTick;
    }

    /**
     * Called each tick as long as this interaction is active
     * @return true to cancel this interaction
     */
    public boolean update() {
        if(this.getSettler().isSneaking() != this.isSneak()) {
            this.getSettler().setSneaking(this.isSneak());
        }
        this.process();
        this.currentTick++;
        return this.getUsageTicks() >= this.getMaxUsageTicks();
    }

    protected abstract void process();

    public static SettlerInteraction interactWithItem(SettlerInteractionController controller, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        return new SettlerInteraction(controller, hand, leftClick, sneak, usageTicks) {
            @Override
            protected void process() {
                if(this.isLeftClick()) {
                    this.getController().leftClickAir();
                } else {
                    this.getController().rightClickAir(this.getHand());
                }
            }
        };
    }

    public static SettlerInteraction interactWithEntity(SettlerInteractionController controller, Entity entity, Vec3d hit, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        return new SettlerInteraction(controller, hand, leftClick, sneak, usageTicks) {
            @Override
            protected void process() {
                if(this.isLeftClick()) {
                    this.getController().leftClickEntity(entity);
                } else {
                    this.getController().rightClickEntity(entity, this.getHand(), hit);
                }
            }
        };
    }

    public static SettlerInteraction interactWitBlock(SettlerInteractionController controller, BlockPos pos, EnumFacing face, Vec3d hit, EnumHand hand, boolean leftClick, boolean sneak, int usageTicks) {
        return new SettlerInteraction(controller, hand, leftClick, sneak, usageTicks) {
            @Override
            protected void process() {
                if(this.isLeftClick()) {
                    this.getController().leftClickBlock(pos, face, hit);
                } else {
                    this.getController().rightClickBlock(pos, face, hit, this.getHand());
                }
            }
        };
    }
}
