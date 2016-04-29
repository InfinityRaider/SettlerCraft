package com.InfinityRaider.settlercraft.utility.schematic;

import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchematicWriter {
    private static final SchematicWriter INSTANCE = new SchematicWriter();

    private final Gson gson;

    private World world;
    private BlockPos first;
    private BlockPos second;

    public static SchematicWriter getInstance() {
        return INSTANCE;
    }

    private SchematicWriter() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void onBlockClicked(World world, BlockPos pos) {
        if(this.world == null || this.world != world) {
            this.world = world;
            first = pos;
            second = null;
        } else {
            if(first != null) {
                second = pos;
                writeSchematic();
            } else {
                first = pos;
            }
        }
    }

    private void writeSchematic() {
        int minX = Math.min(first.getX(), second.getX());
        int minY = Math.min(first.getY(), second.getY());
        int minZ = Math.min(first.getZ(), second.getZ());
        int maxX = Math.max(first.getX(), second.getX());
        int maxY = Math.max(first.getY(), second.getY());
        int maxZ = Math.max(first.getZ(), second.getZ());
        int dx = maxX - minX -1;
        int dy = maxY - minY -1;
        int dz = maxZ - minZ -1;
        first = null;
        second = null;
        BlockPos start = new BlockPos(minX+1, minY+1, minZ+1);
        List<Schematic.BlockPosition> list = new ArrayList<>();
        for(int x = 0; x < dx; x++) {
            for(int y = 0; y < dy ; y++) {
                for(int z = 0; z < dz ; z++) {
                    BlockPos pos = start.add(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if(block == null || block instanceof BlockAir) {
                        continue;
                    }
                    int worldMeta = block.getMetaFromState(state);
                    int stackMeta = block.damageDropped(state);
                    TileEntity tile = world.getTileEntity(pos);
                    NBTTagCompound tag = null;
                    if(tile != null) {
                        tag = new NBTTagCompound();
                        tile.writeToNBT(tag);
                    }
                    Schematic.BlockPosition position = new Schematic.BlockPosition(x, y, z, Block.blockRegistry.getNameForObject(block).toString(), worldMeta, stackMeta, tag);
                    if(needsSupportBlock(block)) {
                        position.setNeedsSupportBlock();
                    }
                    list.add(position.setRotationMetaTransforms(SchematicRotationTransformer.getInstance().getRotationData(block, worldMeta)));
                }
            }
        }
        try {
            serialize(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean needsSupportBlock(Block block) {
        return SchematicRotationTransformer.getInstance().needsSupportBlock(block);
    }

    private void serialize(List<Schematic.BlockPosition> list) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        String path = ConfigurationHandler.getInstance().schematicOutput + "schematic_" + dateFormat.format(new Date()) + ".json";
        Writer writer = new OutputStreamWriter(new FileOutputStream(path));
        gson.toJson(new Schematic(list, 0, new int[] {0, 0, 0}), writer);
        writer.close();
        SchematicReader.getInstance().lastPath = path;
    }
}
