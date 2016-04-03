package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.util.math.BlockPos;

public class SettlerAIRoutineIdle extends SettlerAIRoutine {
    private static final int IDLE_COOL_DOWN = 100;

    private int idleCoolDown;
    private ISettlementBuilding workPlace;


    protected SettlerAIRoutineIdle(EntitySettler settler) {
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
    public void resetRoutine() {}

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
                getSettler().getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, 1);
            }
        }
    }
}
