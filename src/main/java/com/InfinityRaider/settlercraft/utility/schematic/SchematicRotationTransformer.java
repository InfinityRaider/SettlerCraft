package com.InfinityRaider.settlercraft.utility.schematic;

import com.InfinityRaider.settlercraft.api.v1.IRotatableTile;
import com.InfinityRaider.settlercraft.api.v1.ISchematicRotationTransformer;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;

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
    private final List<Block> supportedBlocks;

    private SchematicRotationTransformer() {
        metaTransforms = new HashMap<>();
        supportedBlocks = new ArrayList<>();
    }

    @Override
    public BlockPos applyRotation(BlockPos start, int x, int y, int z, int rotation) {
        switch(rotation) {
            case 1: return start.add(- z, y, x);
            case 2: return start.add(- x, y, -z);
            case 3: return start.add(z, y, - x);
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
    public void registerBlockWithSupport(Block block) {
        if(!supportedBlocks.contains(block)) {
            supportedBlocks.add(block);
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
        if(!metaTransforms.containsKey(block)) {
            return null;
        }
        for(int[] transforms : metaTransforms.get(block)) {
            if(transforms == null) {
                continue;
            }
            for (int transform : transforms) {
                if (transform == meta) {
                    return transforms;
                }
            }
        }
        return null;
    }

    @Override
    public boolean needsSupportBlock(Block block) {
        return supportedBlocks.contains(block);
    }

    public NBTTagCompound rotateTileTag(TileEntity tile, NBTTagCompound tag, int rotation) {
        if(rotation == 0 || tile == null || tag == null) {
            return tag;
        }
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
        //support blocks
        INSTANCE.registerBlockWithSupport(Blocks.TORCH);
        INSTANCE.registerBlockWithSupport(Blocks.REDSTONE_TORCH);
        INSTANCE.registerBlockWithSupport(Blocks.LEVER);
        INSTANCE.registerBlockWithSupport(Blocks.STONE_BUTTON);
        INSTANCE.registerBlockWithSupport(Blocks.WOODEN_BUTTON);
        INSTANCE.registerBlockWithSupport(Blocks.TRIPWIRE_HOOK);
        INSTANCE.registerBlockWithSupport(Blocks.POWERED_COMPARATOR);
        INSTANCE.registerBlockWithSupport(Blocks.UNPOWERED_COMPARATOR);
        INSTANCE.registerBlockWithSupport(Blocks.POWERED_REPEATER);
        INSTANCE.registerBlockWithSupport(Blocks.UNPOWERED_REPEATER);
        INSTANCE.registerBlockWithSupport(Blocks.TRAPDOOR);
        INSTANCE.registerBlockWithSupport(Blocks.IRON_TRAPDOOR);
        INSTANCE.registerBlockWithSupport(Blocks.STONE_PRESSURE_PLATE);
        INSTANCE.registerBlockWithSupport(Blocks.WOODEN_PRESSURE_PLATE);
        INSTANCE.registerBlockWithSupport(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        INSTANCE.registerBlockWithSupport(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
        INSTANCE.registerBlockWithSupport(Blocks.OAK_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.ACACIA_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.BIRCH_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.DARK_OAK_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.IRON_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.JUNGLE_DOOR);
        INSTANCE.registerBlockWithSupport(Blocks.IRON_TRAPDOOR);
        INSTANCE.registerBlockWithSupport(Blocks.LADDER);
        INSTANCE.registerBlockWithSupport(Blocks.VINE);

        //LOGs
        INSTANCE.registerMetaTransforms(Blocks.LOG, new int[]{4, 8, 4, 8});
        INSTANCE.registerMetaTransforms(Blocks.LOG, new int[]{5, 9, 5, 9});
        INSTANCE.registerMetaTransforms(Blocks.LOG, new int[]{6, 10, 6, 10});
        INSTANCE.registerMetaTransforms(Blocks.LOG, new int[]{7, 11, 7, 11});
        INSTANCE.registerMetaTransforms(Blocks.LOG2, new int[]{4, 8, 4, 8});
        INSTANCE.registerMetaTransforms(Blocks.LOG2, new int[]{5, 9, 5, 9});

        //torches, buttons & levers
        int[] torch = new int[] {3, 2, 4, 1};
        INSTANCE.registerMetaTransforms(Blocks.TORCH, torch);
        INSTANCE.registerMetaTransforms(Blocks.REDSTONE_TORCH, torch);
        INSTANCE.registerMetaTransforms(Blocks.WOODEN_BUTTON, torch);
        INSTANCE.registerMetaTransforms(Blocks.STONE_BUTTON, torch);
        INSTANCE.registerMetaTransforms(Blocks.LEVER, torch);

        //active lever
        int[] lever = new int[] {11, 10, 12, 9};
        INSTANCE.registerMetaTransforms(Blocks.LEVER, lever);

        //stairs
        int[] stairs1 = new int[] {2, 1, 3, 0};
        int[] stairs2 = new int[] {6, 5, 7, 4};
        INSTANCE.registerMetaTransforms(Blocks.OAK_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.OAK_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.STONE_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.STONE_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.STONE_BRICK_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.STONE_BRICK_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.BRICK_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.BRICK_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.NETHER_BRICK_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.NETHER_BRICK_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.QUARTZ_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.QUARTZ_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.SANDSTONE_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.SANDSTONE_STAIRS, stairs2);
        INSTANCE.registerMetaTransforms(Blocks.RED_SANDSTONE_STAIRS, stairs1);
        INSTANCE.registerMetaTransforms(Blocks.RED_SANDSTONE_STAIRS, stairs2);

        //misc
        int[] misc = new int[] {5, 3, 4, 2};
        INSTANCE.registerMetaTransforms(Blocks.LADDER, misc);
        INSTANCE.registerMetaTransforms(Blocks.CHEST, misc);
        INSTANCE.registerMetaTransforms(Blocks.TRAPPED_CHEST, misc);
        INSTANCE.registerMetaTransforms(Blocks.ENDER_CHEST, misc);
        INSTANCE.registerMetaTransforms(Blocks.DISPENSER, misc);
        INSTANCE.registerMetaTransforms(Blocks.DROPPER, misc);
        INSTANCE.registerMetaTransforms(Blocks.HOPPER, misc);
        INSTANCE.registerMetaTransforms(Blocks.FURNACE, misc);
        INSTANCE.registerMetaTransforms(Blocks.SKULL, misc);
        INSTANCE.registerMetaTransforms(Blocks.WALL_BANNER, misc);
        INSTANCE.registerMetaTransforms(Blocks.WALL_SIGN, misc);
        INSTANCE.registerMetaTransforms(Blocks.PISTON, misc);
        INSTANCE.registerMetaTransforms(Blocks.STICKY_PISTON, misc);
        INSTANCE.registerMetaTransforms(Blocks.PISTON_HEAD, misc);

        //extended piston
        int[] piston = new int[] {13, 11, 12, 10};
        INSTANCE.registerMetaTransforms(Blocks.PISTON, piston);
        INSTANCE.registerMetaTransforms(Blocks.PISTON_HEAD, piston);

        //vines (don't ask me why this is different compared to ladders)
        int[] vine = new int[] {4, 8, 1, 2};
        INSTANCE.registerMetaTransforms(Blocks.VINE, vine);

        //these actually follow LOGical rotations
        int[] rot1 = new int[] {0, 1, 2, 3};
        int[] rot2 = new int[] {4, 5, 6, 7};
        int[] rot3 = new int[] {8, 9, 10, 11};
        int[] rot4 = new int[] {12, 13, 14, 15};
        INSTANCE.registerMetaTransforms(Blocks.OAK_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.OAK_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.IRON_DOOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.IRON_DOOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.TRIPWIRE_HOOK, rot1);
        INSTANCE.registerMetaTransforms(Blocks.TRIPWIRE_HOOK, rot2);
        INSTANCE.registerMetaTransforms(Blocks.ANVIL, rot1);
        INSTANCE.registerMetaTransforms(Blocks.ANVIL, rot2);
        INSTANCE.registerMetaTransforms(Blocks.ANVIL, rot3);
        INSTANCE.registerMetaTransforms(Blocks.OAK_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.OAK_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.BIRCH_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.SPRUCE_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.JUNGLE_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.ACACIA_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_FENCE_GATE, rot1);
        INSTANCE.registerMetaTransforms(Blocks.DARK_OAK_FENCE_GATE, rot2);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_REPEATER, rot1);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_REPEATER, rot2);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_REPEATER, rot3);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_REPEATER, rot4);
        INSTANCE.registerMetaTransforms(Blocks.POWERED_REPEATER, rot1);
        INSTANCE.registerMetaTransforms(Blocks.POWERED_REPEATER, rot2);
        INSTANCE.registerMetaTransforms(Blocks.POWERED_REPEATER, rot3);
        INSTANCE.registerMetaTransforms(Blocks.POWERED_REPEATER, rot4);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_COMPARATOR, rot1);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_COMPARATOR, rot2);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_COMPARATOR, rot3);
        INSTANCE.registerMetaTransforms(Blocks.UNPOWERED_COMPARATOR, rot4);
        INSTANCE.registerMetaTransforms(Blocks.BED, rot1);
        INSTANCE.registerMetaTransforms(Blocks.BED, rot3);

        //sign & banner
        int[] sign1 = new int[] {12, 0, 4, 8};
        INSTANCE.registerMetaTransforms(Blocks.STANDING_SIGN, sign1);
        INSTANCE.registerMetaTransforms(Blocks.STANDING_BANNER, sign1);

        //trap doors
        int[] trap1 = new int[] {11, 9, 10, 8};
        int[] trap2 = new int[] {15, 13, 14, 12};
        INSTANCE.registerMetaTransforms(Blocks.TRAPDOOR, trap1);
        INSTANCE.registerMetaTransforms(Blocks.TRAPDOOR, trap2);
        INSTANCE.registerMetaTransforms(Blocks.IRON_TRAPDOOR, trap1);
        INSTANCE.registerMetaTransforms(Blocks.IRON_TRAPDOOR, trap2);
    }
}
