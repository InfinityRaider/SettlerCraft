package com.infinityraide.settlercraft.api.v1;

import net.minecraft.util.math.Vec3d;

public interface ISettlerActionTarget {
    Vec3d getTarget(ISettler settler);
}
