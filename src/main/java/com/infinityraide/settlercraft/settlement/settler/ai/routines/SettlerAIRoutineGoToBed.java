package com.infinityraide.settlercraft.settlement.settler.ai.routines;

import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class SettlerAIRoutineGoToBed extends SettlerAIRoutine {
    private boolean pathFinding;
    private BlockPos target;
    private int bedIndex;

    public SettlerAIRoutineGoToBed(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GOING_TO_BED);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().settlement() != null && !getSettler().getWorld().isDaytime();
    }

    @Override
    public boolean continueExecutingRoutine() {
        return shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {

    }

    @Override
    public void interruptRoutine() {
        pathFinding = false;
        target = null;
        bedIndex = -1;
    }

    @Override
    public void updateRoutine() {
        if(!getSettler().isSleeping()) {
            ISettlementBuilding building = getSettler().home();
            boolean hasHome = true;
            if(building == null) {
                building = getSettler().settlement().getTownHall();
                hasHome = false;
            }
            if(building == null) {
                return;
            }
            //Get the target position for the settler to move to
            List<BlockPos> beds = building.getBeds();
            if(target == null) {
                if (!isHome()) {
                    target = building.homePosition();
                } else {
                    target = beds.get(bedIndex);
                }
            }
            if(target == null) {
                return;
            }
            //If the settler is within reach of its target, interact with the target, or set the next target
            double dist = this.getDistanceFromPositionSquared(target);
            double reach = 2.5;
            if(dist <= reach * reach) {
                pathFinding = false;
                //If the settler has moved to a town hall, nothing has to happen anymore
                if(!hasHome) {
                    return;
                }
                //If the settler has reached its home, find a bed to sleep in
                if(isHome()) {
                    if(getSettler().trySleepInBed(target) == EntityPlayer.SleepResult.OK && (beds.size() > bedIndex + 1)) {
                        bedIndex = bedIndex + 1;
                    }
                }
                target = null;
            }
            //Find path to the target if not already doing so
            else if(!pathFinding) {
                super.findPathToTarget(target);
                this.pathFinding = true;
            }
        }
    }

    private boolean isHome() {
        return bedIndex >= 0;
    }
}
