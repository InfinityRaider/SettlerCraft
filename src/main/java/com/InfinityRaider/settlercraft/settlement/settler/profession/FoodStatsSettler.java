package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FoodStatsSettler extends FoodStats {
    /** The player's food level. */
    private int foodLevel = 20;
    /** The player's food saturation. */
    private float foodSaturationLevel = 5.0F;
    /** The player's food exhaustion. */
    private float foodExhaustionLevel;
    /** The player's food timer value. */
    private int foodTimer;

    private final EntitySettler settler;

    public FoodStatsSettler(EntitySettler settler) {
        super();
        this.settler = settler;
    }

    public EntitySettler getSettler() {
        return settler;
    }

    /**
     * Add food stats.
     */
    @Override
    public void addStats(int foodLevelIn, float foodSaturationModifier) {
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.foodLevel);
    }

    @Override
    public void addStats(ItemFood foodItem, ItemStack stack) {
        this.addStats(foodItem.getHealAmount(stack), foodItem.getSaturationModifier(stack));
    }

    /**
     * Handles the food game logic.
     */
    @Override
    public void onUpdate(EntityPlayer player) {
        this.onUpdate();
    }

    public void onUpdate() {
        EnumDifficulty enumdifficulty = getSettler().worldObj.getDifficulty();
        if (this.foodExhaustionLevel > 4.0F) {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
        boolean flag = getSettler().worldObj.getGameRules().getBoolean("naturalRegeneration");
        if (flag && this.foodSaturationLevel > 0.0F && getSettler().shouldHeal() && this.foodLevel >= 20) {
            ++this.foodTimer;
            if (this.foodTimer >= 10) {
                float f = Math.min(this.foodSaturationLevel, 4.0F);
                getSettler().heal(f / 4.0F);
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && getSettler().shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                getSettler().heal(1.0F);
                this.addExhaustion(4.0F);
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (getSettler().getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || getSettler().getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)                {
                    getSettler().attackEntityFrom(DamageSource.starve, 1.0F);
                } this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }
    }

    /**
     * Reads the food data for the player.
     */
    @Override
    public void readNBT(NBTTagCompound compound) {
        if (compound.hasKey("foodLevel", 99)) {
            this.foodLevel = compound.getInteger("foodLevel");
            this.foodTimer = compound.getInteger("foodTickTimer");
            this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
        }
    }

    /**
     * Writes the food data for the player.
     */
    @Override
    public void writeNBT(NBTTagCompound compound) {
        compound.setInteger("foodLevel", this.foodLevel);
        compound.setInteger("foodTickTimer", this.foodTimer);
        compound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        compound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    /**
     * Get the player's food level.
     */
    @Override
    public int getFoodLevel() {
        return this.foodLevel;
    }

    /**
     * Get whether the player must eat food.
     */
    @Override
    public boolean needFood() {
        return this.foodLevel < 20;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    @Override
    public void addExhaustion(float exhaustion) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
    }

    /**
     * Get the player's food saturation level.
     */
    @Override
    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }

    @Override
    public void setFoodLevel(int foodLevelIn) {
        this.foodLevel = foodLevelIn;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodSaturationLevel(float foodSaturationLevelIn) {
        this.foodSaturationLevel = foodSaturationLevelIn;
    }
}
