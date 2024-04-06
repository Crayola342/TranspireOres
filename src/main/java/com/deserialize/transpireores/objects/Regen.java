package com.deserialize.transpireores.objects;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Regen {
    private long end;
    private long start;
    private Block block;
    private Location loc;
    private RegenerateOre ore;

    public Regen(Block block, RegenerateOre ore, long start, long end) {
        this.start = start;
        this.end = end;
        this.block = block;
        this.loc = block.getLocation();
        this.ore = ore;
    }

    public RegenerateOre getOre() {
        return this.ore;
    }

    public long getStart() {
        return this.start;
    }

    public long getEnd() {
        return this.end;
    }

    public Block getBlock() {
        return this.block;
    }

    public Location getLoc() {
        return this.loc;
    }

    public void regen() {
        this.block.setType(this.ore.getOreSpawn());
    }
}