package net.lazyio.oretree.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.lazyio.oretree.api.IOreTree;
import net.lazyio.oretree.api.OreTreesAPI;
import net.lazyio.oretree.api.SimpleOreTree;
import net.lazyio.oretree.block.OreLeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OreTreeAPIImpl implements OreTreesAPI {

    private static final HashMap<ResourceLocation, IOreTree> ORES_TREES = new HashMap<>();
    private static final List<OreLeavesBlock> ORE_LEAVES_TINT = new ArrayList<>();

    @Override
    public void applyOreLeavesForBlockTint(OreLeavesBlock oreLeavesBlock) {
        ORE_LEAVES_TINT.add(oreLeavesBlock);
    }

    @Override
    public ImmutableList<OreLeavesBlock> getAppliedOreLeavesForTint() {
        return ImmutableList.copyOf(ORE_LEAVES_TINT);
    }

    @Override
    public void registerOre(ResourceLocation id, BlockState ore) {
        this.registerOre(id, new SimpleOreTree(ore));
    }

    @Override
    public void registerOre(ResourceLocation id, IOreTree oreIn) {
        if (!ORES_TREES.containsKey(id)) {
            ORES_TREES.put(id, oreIn);
        }
    }

    @Override
    public ImmutableMap<ResourceLocation, IOreTree> getRegisteredOreTrees() {
        return ImmutableMap.copyOf(ORES_TREES);
    }

    @Override
    public ImmutableList<IOreTree> getOreTrees() {
        return ImmutableList.copyOf(ORES_TREES.values());
    }
}
