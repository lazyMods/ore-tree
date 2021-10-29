package net.lazyio.oretree.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.lazyio.oretree.block.OreLeavesBlock;
import net.lazyio.oretree.impl.OreTreeAPIImpl;
import net.minecraft.block.BlockState;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;

public interface OreTreesAPI {

    /**
     * This is based on the BotaniaAPI system.
     * Check here -> https://github.com/VazkiiMods/Botania/blob/master/src/main/java/vazkii/botania/api/BotaniaAPI.java
     */
    LazyValue<OreTreesAPI> API = new LazyValue<>(() -> {
        try {
            return OreTreeAPIImpl.class.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            System.err.printf("Something went wrong when initializing the OreTreesAPI. Exception msg: %s\n", e.getMessage());
            return new OreTreesAPI() {};
        }
    });


    static OreTreesAPI get() {
        return API.get();
    }

    /**
     * Apply your instance of {@link OreLeavesBlock} to be tinted with the given
     * color.
     *
     * @param oreLeavesBlock - the block to apply the block colors.
     */
    default void applyOrelLeavesForBlockTint(OreLeavesBlock oreLeavesBlock) {
        //NOOP
    }

    /**
     * The list of {@link OreLeavesBlock} that applied for tinting.
     * <p>
     *
     * @return the applied blocks for tinting.
     */
    default ImmutableList<OreLeavesBlock> getAppliedOreLeavesForTint() {
        return ImmutableList.of();
    }

    /**
     * Adds a {@link SimpleOreTree} that have most of the values
     * already defined.
     * <p>
     * log -> OAK_LOG
     * leaves -> OAK_LEAVES
     * chance -> 1f
     * sphereRadius -> 5
     *
     * @param id  - unique id, mainly for compatibility.
     * @param ore - The block that is considered an ore.
     */
    default void registerOre(ResourceLocation id, BlockState ore) {
        //NOOP
    }

    /**
     * Adds a new {@link IOreTree}.
     *
     * @param oreTree - an {@link IOreTree} implementation.
     * @param id      - unique id, mainly for compatibility.
     */
    default void registerOre(ResourceLocation id, IOreTree oreTree) {
        //NOOP
    }

    default ImmutableMap<ResourceLocation, IOreTree> getRegisteredOreTrees() {
        return ImmutableMap.of();
    }


    /**
     * Returns a list with all the registered {@link IOreTree}
     *
     * @return a list of {@link IOreTree}
     */
    default ImmutableList<IOreTree> getOreTrees() {
        return ImmutableList.of();
    }
}
