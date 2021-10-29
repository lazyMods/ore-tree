package net.lazyio.oretree.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;

public class OreLeavesBlock extends LeavesBlock {

    private final int color;

    public OreLeavesBlock(int color) {
        super(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()
                .isValidSpawn((i, j, l, entityType) -> entityType == EntityType.OCELOT || entityType == EntityType.PARROT)
                .isSuffocating((i, j, l) -> false)
                .isViewBlocking((i, j, l) -> false));
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}
