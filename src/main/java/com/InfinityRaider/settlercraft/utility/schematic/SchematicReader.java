package com.InfinityRaider.settlercraft.utility.schematic;

import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SchematicReader {
    private static final SchematicReader INSTANCE = new SchematicReader();

    public static SchematicReader getInstance() {
        return INSTANCE;
    }

    private final Gson gson;

    private SchematicReader() {
        gson = new Gson();
    }

    public void buildStoredSchematic(World world, BlockPos pos, int rotation) {
        List<Schematic.BlockPosition> list;
        try {
            list = deserialize(ConfigurationHandler.getInstance().schematicOutput).blocks;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        List<Schematic.BlockPosition> delayedBlocks = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            Schematic.BlockPosition position = list.get(i);
            if(position.needsSupportBlock()) {
                delayedBlocks.add(position);
            } else {
                boolean flag = delayedBlocks.size() == 0 && i == (list.size() - 1);
                addBlockPositionToWorld(world, position, pos, rotation,  flag);
            }
        }
        for(int i = 0; i < delayedBlocks.size(); i++) {
            addBlockPositionToWorld(world, delayedBlocks.get(i), pos, rotation, i == (list.size()-1) );
        }
    }

    private void addBlockPositionToWorld(World world, Schematic.BlockPosition data, BlockPos pos, int rotation, boolean blockUpdate) {
        Block block = Block.blockRegistry.getObject(new ResourceLocation(data.block));
        int meta = data.getWorldMeta(rotation);
        IBlockState state = block.getStateFromMeta(meta);
        int flag = blockUpdate ? 3 : 2;
        BlockPos newPos = SchematicRotationTransformer.getInstance().applyRotation(pos, data.x, data.y, data.z, rotation);
        world.setBlockState(newPos, state, flag);
        NBTTagCompound tag = data.getTag();
        if(tag != null) {
            TileEntity tile = world.getTileEntity(newPos);
            if(tile != null) {
                tag.setInteger("x", newPos.getX());
                tag.setInteger("y", newPos.getY());
                tag.setInteger("z", newPos.getZ());
                SchematicRotationTransformer.getInstance().rotateTileTag(tile, tag, rotation);
                tile.readFromNBT(tag);
            }
        }

    }

    public Schematic deserialize(ResourceLocation location) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(getFilePath(location)), "UTF-8"));
        return deserialize(reader);
    }

    public Schematic deserialize(String filePath) throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(filePath));
        return deserialize(reader);
    }

    public Schematic deserialize(Reader reader) throws IOException {
        Schematic schematic = gson.fromJson(reader, Schematic.class);
        reader.close();
        return schematic;

    }

    private String getFilePath(ResourceLocation location) {
        return "assets/" + location.getResourceDomain() + "/" + location.getResourcePath() + ".json";
    }
}
