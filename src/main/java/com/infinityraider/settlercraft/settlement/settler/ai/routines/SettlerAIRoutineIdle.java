package com.infinityraider.settlercraft.settlement.settler.ai.routines;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.api.v1.ITask;
import com.infinityraider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.util.math.BlockPos;

public class SettlerAIRoutineIdle extends SettlerAIRoutine {
    private static final int IDLE_COOL_DOWN = 100;

    private int idleCoolDown;
    private boolean pathFinding;

    public SettlerAIRoutineIdle(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.IDLE);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return true;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return false;
    }

    @Override
    public void startExecutingRoutine() {}

    @Override
    public void interruptRoutine() {}

    @Override
    public void updateRoutine() {
        if(getWorkPlace() == null) {
            if (idleCoolDown == 0) {
                idleCoolDown = IDLE_COOL_DOWN;
                ISettlement settlement = getSettler().settlement();
                if(settlement != null) {
                    settlement.getBuildings().stream().filter(building -> building.canDoWorkHere(getSettler())).forEach(this::setWorkPlace);
                }
            }
            idleCoolDown = idleCoolDown - 1;
            pathFinding = false;
        } else {
            BlockPos target = getWorkPlace().homePosition();
            if(getDistanceFromPositionSquared(target) <= 6) {
                ITask task = getSettler().workPlace().getTaskForSettler(getSettler());
                if(task != null) {
                    this.getSettler().assignTask(task);
                }
                pathFinding = false;
            } else if(!pathFinding || getSettler().getNavigator().noPath()) {
                pathFinding = getSettler().getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1);
            }
        }
    }

    private ISettlementBuilding getWorkPlace() {
        return getSettler().workPlace();
    }

    private void setWorkPlace(ISettlementBuilding building) {
        getSettler().setWorkPlace(building);
    }
}
