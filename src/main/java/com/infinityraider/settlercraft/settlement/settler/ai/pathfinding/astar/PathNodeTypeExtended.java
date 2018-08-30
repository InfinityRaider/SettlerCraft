package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding.astar;

import net.minecraft.pathfinding.PathNodeType;
import net.minecraftforge.common.util.EnumHelper;

public enum PathNodeTypeExtended {
    BLOCKED(PathNodeType.BLOCKED),
    OPEN(PathNodeType.OPEN),
    WALKABLE(PathNodeType.WALKABLE),
    TRAPDOOR(PathNodeType.TRAPDOOR),
    FENCE(PathNodeType.FENCE),
    LAVA(PathNodeType.LAVA),
    WATER(PathNodeType.WATER),
    RAIL(PathNodeType.RAIL),
    DANGER_FIRE(PathNodeType.DANGER_FIRE),
    DAMAGE_FIRE(PathNodeType.DAMAGE_FIRE),
    DANGER_CACTUS(PathNodeType.DANGER_CACTUS),
    DAMAGE_CACTUS(PathNodeType.DAMAGE_CACTUS),
    DANGER_OTHER(PathNodeType.DANGER_OTHER),
    DAMAGE_OTHER(PathNodeType.DAMAGE_OTHER),
    DOOR_OPEN(PathNodeType.DOOR_OPEN),
    DOOR_WOOD_CLOSED(PathNodeType.DOOR_WOOD_CLOSED),
    DOOR_IRON_CLOSED(PathNodeType.DOOR_IRON_CLOSED),
    LADDER("LADDER", 0.0F);

    private final PathNodeType type;

    PathNodeTypeExtended(PathNodeType type) {
        this.type = type;
    }

    PathNodeTypeExtended(String name, float priority) {
        this(EnumHelper.addEnum(PathNodeType.class, name, new Class[]{Float.class}, priority));
    }

    public PathNodeType getType() {
        return this.type;
    }

    public float getPriority() {
        return getType().getPriority();
    }
}
