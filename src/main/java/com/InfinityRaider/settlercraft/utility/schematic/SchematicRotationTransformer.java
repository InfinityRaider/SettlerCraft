package com.InfinityRaider.settlercraft.utility.schematic;

import com.InfinityRaider.settlercraft.api.v1.IRotatableTile;
import com.InfinityRaider.settlercraft.api.v1.ISchematicRotationTransformer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchematicRotationTransformer implements ISchematicRotationTransformer {
    private static final SchematicRotationTransformer INSTANCE = new SchematicRotationTransformer();

    public static SchematicRotationTransformer getInstance() {
        return INSTANCE;
    }

    private final Map<Block, List<int[]>> metaTransforms;

    private SchematicRotationTransformer() {
        metaTransforms = new HashMap<>();
    }

    @Override
    public BlockPos applyRotation(BlockPos start, int x, int y, int z, int rotation) {
        switch(rotation) {
            case 1: return start.add(-z, y, x);
            case 2: return start.add(-x, y, -z);
            case 3: return start.add(z, y, -x);
            default: return start.add(x, y, z);
        }
    }

    @Override
    public void registerMetaTransforms(Block block, int[] meta) {
        if(meta.length == 4) {
            if(!metaTransforms.containsKey(block)) {
                List<int[]> list = new ArrayList<>();
                list.add(meta);
                metaTransforms.put(block, list);
            } else {
                metaTransforms.get(block).add(meta);
            }
        }
    }

    @Override
    public int transformMeta(Block block, int meta, int rotation) {
        if(!metaTransforms.containsKey(block)) {
            return meta;
        }
        for(int[] transforms : metaTransforms.get(block)) {
            for (int i = 0; i < transforms.length; i++) {
                if (transforms[i] == meta) {
                    return transforms[(i + rotation) % transforms.length];
                }
            }
        }
        return meta;
    }

    @Override
    public int[] getRotationData(Block block, int meta) {
        for(int[] transforms : metaTransforms.get(block)) {
            for (int transform : transforms) {
                if (transform == meta) {
                    return transforms;
                }
            }
        }
        return null;
    }

    public NBTTagCompound rotateTileTag(TileEntity tile, NBTTagCompound tag, int rotation) {
        if(tile instanceof IRotatableTile) {
            ((IRotatableTile) tile).setRotationTag(tag, rotation);
        }
        else if(tile instanceof TileEntitySkull && tag.hasKey("Rot")) {
            int rot =  (tag.getByte("Rot") + (rotation * 4))%16;
            tag.setByte("Rot", (byte) rot);
        }
        return tag;
    }

    static {
        //torches, buttons & levers
        int[] torch = new int[] {3, 2, 4, 1};
        INSTANCE.registerMetaTransforms(Blocks.torch, torch);
        INSTANCE.registerMetaTransforms(Blocks.redstone_torch, torch);
        INSTANCE.registerMetaTransforms(Blocks.wooden_button, torch);
        INSTANCE.registerMetaTransforms(Blocks.stone_button, torch);
        INSTANCE.registerMetaTransforms(Blocks.lever, torch);

        //active lever
        int[] lever = new int[] {11, 10, 12, 9};
        INSTANCE.registerMetaTransforms(Blocks.lever, lever);

        //stairs
        int[] stairs1 = new int[] {2, 1, 3, 0};
        int[] stairs2 = new int[] {6, 5, 7, 4};
        INSTANCE.registerMetaTransforms(Blocks.oak_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.oak_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.birch_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.birch_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.spruce_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.spruce_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.jungle_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.jungle_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.acacia_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.acacia_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.stone_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.stone_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.stone_brick_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.stone_brick_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.brick_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.brick_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.nether_brick_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.nether_brick_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.quartz_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.quartz_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.sandstone_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.sandstone_stairs, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.red_sandstone_stairs, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.red_sandstone_stairs, stairs2);

        //misc
        int[] misc = new int[] {5, 3, 4, 2};
        INSTANCE.registerMetaTransforms(Blocks.ladder, misc);
        INSTANCE.registerMetaTransforms(Blocks.chest, misc);
        INSTANCE.registerMetaTransforms(Blocks.trapped_chest, misc);
        INSTANCE.registerMetaTransforms(Blocks.ender_chest, misc);
        INSTANCE.registerMetaTransforms(Blocks.dispenser, misc);
        INSTANCE.registerMetaTransforms(Blocks.dropper, misc);
        INSTANCE.registerMetaTransforms(Blocks.hopper, misc);
        INSTANCE.registerMetaTransforms(Blocks.furnace, misc);
        INSTANCE.registerMetaTransforms(Blocks.skull, misc);
        INSTANCE.registerMetaTransforms(Blocks.wall_banner, misc);
        INSTANCE.registerMetaTransforms(Blocks.wall_sign, misc);
        INSTANCE.registerMetaTransforms(Blocks.piston, misc);
        INSTANCE.registerMetaTransforms(Blocks.sticky_piston, misc);
        INSTANCE.registerMetaTransforms(Blocks.piston_head, misc);

        //extended piston
        int[] piston = new int[] {13, 11, 12, 10};
        INSTANCE.registerMetaTransforms(Blocks.piston, piston);
        INSTANCE.registerMetaTransforms(Blocks.piston_head, piston);

        //vines (don't ask me why this is different compared to ladders)
        int[] vine = new int[] {4, 8, 1, 2};
        INSTANCE.registerMetaTransforms(Blocks.vine, vine);

        //these actually follow logical rotations
        int[] rot1 = new int[] {0, 1, 2, 3};
        int[] rot2 = new int[] {4, 5, 6, 7};
        int[] rot3 = new int[] {8, 9, 10, 11};
        int[] rot4 = new int[] {12, 13, 14, 15};
        INSTANCE.registerMetaTransforms(Blocks.oak_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.oak_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.birch_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.birch_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.spruce_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.spruce_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.jungle_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.jungle_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.acacia_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.acacia_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.iron_door, rot1);
        INSTANCE.registerMetaTransforms(Blocks.iron_door, rot2);
        INSTANCE.registerMetaTransforms(Blocks.tripwire_hook, rot1);
        INSTANCE.registerMetaTransforms(Blocks.tripwire_hook, rot2);
        INSTANCE.registerMetaTransforms(Blocks.anvil, rot1);
        INSTANCE.registerMetaTransforms(Blocks.anvil, rot2);
        INSTANCE.registerMetaTransforms(Blocks.anvil, rot3);
        INSTANCE.registerMetaTransforms(Blocks.oak_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.oak_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.birch_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.birch_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.spruce_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.spruce_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.jungle_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.jungle_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.acacia_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.acacia_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_fence_gate, rot1);
        INSTANCE.registerMetaTransforms(Blocks.dark_oak_fence_gate, rot2);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_repeater, rot1);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_repeater, rot2);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_repeater, rot3);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_repeater, rot4);
        INSTANCE.registerMetaTransforms(Blocks.powered_repeater, rot1);
        INSTANCE.registerMetaTransforms(Blocks.powered_repeater, rot2);
        INSTANCE.registerMetaTransforms(Blocks.powered_repeater, rot3);
        INSTANCE.registerMetaTransforms(Blocks.powered_repeater, rot4);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_comparator, rot1);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_comparator, rot2);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_comparator, rot3);
        INSTANCE.registerMetaTransforms(Blocks.unpowered_comparator, rot4);
        INSTANCE.registerMetaTransforms(Blocks.bed, rot1);
        INSTANCE.registerMetaTransforms(Blocks.bed, rot3);

        //sign & banner
        int[] sign1 = new int[] {12, 0, 4, 8};
        INSTANCE.registerMetaTransforms(Blocks.standing_sign, sign1);
        INSTANCE.registerMetaTransforms(Blocks.standing_banner, sign1);

        //trap doors
        int[] trap1 = new int[] {11, 9, 10, 8};
        int[] trap2 = new int[] {15, 13, 14, 12};
        INSTANCE.registerMetaTransforms(Blocks.trapdoor, trap1);
        INSTANCE.registerMetaTransforms(Blocks.trapdoor, trap2);
        INSTANCE.registerMetaTransforms(Blocks.iron_trapdoor, trap1);
        INSTANCE.registerMetaTransforms(Blocks.iron_trapdoor, trap2);
    }
}
