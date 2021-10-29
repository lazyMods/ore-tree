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

    public OreTreeImpl(Block log, Block leaves, Block ore, float chance, int blobRadius) {
        this.log = log;
        this.leaves = leaves;
        this.ore = ore;
        this.chance = chance;
        this.blobRadius = blobRadius;
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
}
