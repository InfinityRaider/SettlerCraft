package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding;

import net.minecraft.pathfinding.WalkNodeProcessor;

public class NodeProcessorSettler extends WalkNodeProcessor {
    public NodeProcessorSettler() {
        super();
        this.setCanEnterDoors(true);
    }
}
