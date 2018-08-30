package com.infinityraider.settlercraft.utility.schematic;

import com.infinityraider.settlercraft.SettlerCraft;
import com.infinityraider.settlercraft.handler.ConfigurationHandler;
import com.infinityraider.settlercraft.utility.BoundingBox;
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
        Schematic schematic;
        try {
            File schematicFile = getLastSchematic();
            if(schematicFile == null) {
                return;
            }
            schematic = deserialize(new FileReader(schematicFile));
        } catch (IOException e) {
            SettlerCraft.instance.getLogger().printStackTrace(e);
            return;
        }
        List<Schematic.BlockPosition> list = schematic.blocks;
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
        BoundingBox box = schematic.getBoundingBox(pos, rotation);
        world.markBlockRangeForRenderUpdate(box.getMinimumPosition(), box.getMaximumPosition());
    }

    public File getLastSchematic() {
        File dir = new File(ConfigurationHandler.getInstance().schematicOutput);
        File[] files = dir.listFiles();
        if(files == null) {
            return null;
        }
        File last = null;
        for(File file : files) {
            if(last == null) {
                last = file;
                continue;
            }
            if(last.lastModified() < file.lastModified()) {
                last = file;
            }
        }
        return last;
    }

    private void addBlockPositionToWorld(World world, Schematic.BlockPosition data, BlockPos pos, int rotation, boolean blockUpdate) {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(data.block));
        int meta = data.getWorldMeta(rotation);
        int flag = blockUpdate ? 3 : 2;
        BlockPos newPos = SchematicRotationTransformer.getInstance().applyRotation(pos, data.x, data.y, data.z, rotation);
        IBlockState oldState = world.getBlockState(newPos);
        IBlockState newState = block.getStateFromMeta(meta);
        world.setBlockState(newPos, newState, flag);
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
        world.notifyBlockUpdate(newPos, oldState, newState, 3);
    }

    public Schematic deserialize(ResourceLocation location) throws IOException {
        String filePath = getFilePath(location);
        SettlerCraft.instance.getLogger().debug("Parsing file for "+filePath);
        Reader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath), "UTF-8"));
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
