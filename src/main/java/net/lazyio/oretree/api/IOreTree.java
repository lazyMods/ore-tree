package net.lazyio.oretree.api;

import net.minecraft.block.BlockState;

public interface IOreTree {

    /**
     * The block state that represents the trunk.
     * This is called log because when you think a tree as logs and leaves.
     *
     * @return the tree trunk block.
     */
    BlockState getLog();

    /**
     * Pretty much explanatory.
     * Be aware that if you use a default implementation of LeavesBlock,
     * the getLog() needs to be a block with the WOOD material.
     * If not the leaves will disappear overtime.
     *
     * @return the leaves block
     */
    BlockState getLeaves();

    /**
     * The ore that will generate bellow the tree.
     *
     * @return the ore block
     */
    BlockState getOre();

    /**
     * A value between 0 and 1 that represents the change.
     * 1 being always spawns
     * 0 being never spawns
     *
     * @return the chance
     */
    float getChance();

    /**
     * Below the OreTree is generated a sphere with ores.
     * This method should return that sphere radius.
     *
     * @return sphere radius
     */
    int blobRadius();

    /**
     * When this is set to true, the ore tree will
     * spawn in the Nether if the has netherrack to spawn;
     * @return
     */
    boolean netherSpawn();
}
