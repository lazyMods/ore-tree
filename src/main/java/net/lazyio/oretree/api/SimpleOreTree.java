package net.lazyio.oretree.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class SimpleOreTree implements IOreTree {

    public BlockState ore;

    public SimpleOreTree(BlockState ore) {
        this.ore = ore;
    }

    @Override
    public BlockState getLog() {
        return Blocks.OAK_LOG.defaultBlockState();
    }

    @Override
    public BlockState getLeaves() {
        return Blocks.OAK_LEAVES.defaultBlockState();
    }

    @Override
    public BlockState getOre() {
        return this.ore;
    }

    @Override
    public float getChance() {
        return .5f;
    }

    @Override
    public int blobRadius() {
        return 5;
    }

    @Override
    public List<ResourceLocation> allowedInBiomes() {
        return Collections.emptyList();
    }
}
