package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.util.math.BlockPos;

public class SettlerAIRoutineIdle extends SettlerAIRoutine {
    private static int IDLE_COOL_DOWN = 100;

    private int idleCoolDown;
    private ISettlementBuilding workPlace;


    protected SettlerAIRoutineIdle(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.IDLE);
    }

    @Override
    public ITask getActiveTask() {
        return null;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return true;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {
        this.idleCoolDown = 0;
    }

    @Override
    public void resetRoutine() {
        this.idleCoolDown = IDLE_COOL_DOWN;
    }

    @Override
    public void updateRoutine() {
        if(workPlace == null) {
            if (idleCoolDown == 0) {
                idleCoolDown = IDLE_COOL_DOWN;
                ISettlement settlement = getSettler().settlement();
                if(settlement != null) {
                    settlement.getBuildings().stream().filter(building -> building.canDoWorkHere(getSettler())).forEach(building -> this.workPlace = building);
                }
            }
            idleCoolDown = idleCoolDown - 1;
        } else {
            BlockPos target = workPlace.homePosition();
            if(getDistanceFromPositionSquared(target) <= 6) {
                this.getSettler().assignTask(workPlace.getTaskForSettler(getSettler()));
            } else {
                getSettler().getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, getSettler().getAIMoveSpeed());
            }
        }
    }
}
