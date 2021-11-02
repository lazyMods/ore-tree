package net.lazyio.oretree.impl;

import net.lazyio.oretree.api.IOreTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class OreTreeImpl implements IOreTree {

    private final Block log;
    private final Block leaves;
    private final Block ore;
    private final float chance;
    private final int blobRadius;
    private boolean nether;

    public OreTreeImpl(Block log, Block leaves, Block ore, float chance, int blobRadius, boolean nether) {
        this.log = log;
        this.leaves = leaves;
        this.ore = ore;
        this.chance = chance;
        this.blobRadius = blobRadius;
        this.nether = nether;
    }

    @Override
    public BlockState getLog() {
        return this.log.defaultBlockState();
    }

    @Override
    public BlockState getLeaves() {
        return this.leaves.defaultBlockState();
    }

    @Override
    public BlockState getOre() {
        return this.ore.defaultBlockState();
    }

    @Override
    public float getChance() {
        return this.chance;
    }

    @Override
    public int blobRadius() {
        return this.blobRadius;
    }

    @Override
    public boolean netherSpawn() {
        return this.nether;
    }
}
