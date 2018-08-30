package com.infinityraide.settlercraft.settlement.settler.ai.pathfinding.astar;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AStarTest {
    private static final AStarTest INSTANCE = new AStarTest();

    public static AStarTest getInstance() {
        return INSTANCE;
    }

    private World world;
    private BlockPos start;
    private AStar aStar;
    private BlockPos stop;

    private Node[][][] nodes;

    private List<Node> path;

    private AStarTest() {}

    public void onRightClick(World world, BlockPos pos, EntitySettler settler) {
        if(this.world != null && world != this.world) {
            return;
        }
        if(path == null) {
            if (start == null) {
                this.world = world;
                this.start = pos;
            } else {
                this.stop = pos;
                if(aStar == null) {
                    this.aStar = new AStar(settler, start, stop, 5, 2).setMaxFallHeight(3).setSwim(true).setClimbLadders(true);
                }
                nodes = aStar.buildNodes();
                path = aStar.run();
            }
        }
    }

    public List<Node> getPath() {
        return path;
    }

    public Node[][][] getNodes() {
        return nodes;
    }

    public void reset() {
        this.world = null;
        this.start = null;
        this.stop = null;
        this.path = null;
        this.nodes = null;
        this.aStar = null;
    }
}
