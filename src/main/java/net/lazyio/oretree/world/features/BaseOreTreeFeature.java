package net.lazyio.oretree.world.features;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.lazyio.oretree.api.IOreTree;
import net.lazyio.oretree.api.OreTreesAPI;
import net.lazyio.oretree.world.config.OreTreeConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;

public abstract class BaseOreTreeFeature extends Feature<OreTreeConfig> {

    public BaseOreTreeFeature() {
        super(OreTreeConfig.CODEC);
    }

    public abstract BlockPos calculateGenPos(ISeedReader reader, BlockPos pos, OreTreeConfig config);
    public abstract boolean isValidSoil(IWorldGenerationBaseReader reader, BlockPos pos, OreTreeConfig config, IOreTree oreTree);

    protected boolean isFree(IWorldGenerationBaseReader reader, BlockPos pos) {
        return this.validTreePos(reader, pos) || reader.isStateAtPosition(pos, state -> state.is(BlockTags.LOGS));
    }

    protected boolean isBlockWater(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, state -> state.is(Blocks.WATER));
    }

    protected boolean isAirOrLeaves(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, state -> state.getBlock() == Blocks.AIR || state.is(BlockTags.LEAVES));
    }

    protected boolean isReplaceablePlant(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, state -> state.getMaterial() == Material.REPLACEABLE_PLANT);
    }

    protected void setBlockKnownShape(IWorldWriter worldWriter, BlockPos pos, BlockState state) {
        worldWriter.setBlock(pos, state, 19);
    }

    protected boolean validTreePos(IWorldGenerationBaseReader reader, BlockPos pos) {
        return this.isAirOrLeaves(reader, pos) || this.isReplaceablePlant(reader, pos) || this.isBlockWater(reader, pos);
    }

    private boolean doPlace(ISeedReader seedReader, IWorldGenerationReader reader, Random random, BlockPos pos, Set<BlockPos> posSet, Set<BlockPos> posSet2, MutableBoundingBox boundingBox, OreTreeConfig featureConfig) {

        IOreTree oreTree = OreTreesAPI.get().getOreTrees().get(random.nextInt(OreTreesAPI.get().getOreTrees().size() - 1));

        if (!oreTree.allowedInBiomes().contains(featureConfig.biomesRegKey)) {
            return false;
        }

        int treeHeight = featureConfig.trunkAndOresPlacer.getTreeHeight(random);
        int foliageHeight = featureConfig.foliagePlacer.foliageHeight();
        int difHeight = treeHeight - foliageHeight;
        int foliageRadius = featureConfig.foliagePlacer.foliageRadius(random, difHeight);

        BlockPos blockpos = this.calculateGenPos(seedReader, pos, featureConfig);

        if (blockpos.getY() >= 1 && blockpos.getY() + treeHeight + 1 <= 256) {

            if (!this.isValidSoil(reader, blockpos.below(), featureConfig, oreTree)) {
                return false;
            } else {
                OptionalInt minClippedHeight = featureConfig.twoLayerFeature.minClippedHeight();
                int maxFreeTreeHeight = this.getMaxFreeTreeHeight(reader, treeHeight, blockpos, featureConfig);
                if (maxFreeTreeHeight >= treeHeight || minClippedHeight.isPresent() && maxFreeTreeHeight >= minClippedHeight.getAsInt()) {
                    List<FoliagePlacer.Foliage> foliageList = featureConfig.trunkAndOresPlacer.placeTrunk(oreTree, reader, random, maxFreeTreeHeight, blockpos, posSet, boundingBox);
                    foliageList.forEach((foliage) -> featureConfig.foliagePlacer.placeFoliage(oreTree, reader, random, foliage, foliageHeight, foliageRadius, posSet2, 0, boundingBox));
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(IWorldGenerationBaseReader reader, int height, BlockPos pos, OreTreeConfig config) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();

        for (int i = 0; i <= height + 1; ++i) {
            int j = config.twoLayerFeature.getSizeAtHeight(height, i);
            for (int k = -j; k <= j; ++k) {
                for (int l = -j; l <= j; ++l) {
                    blockPos.setWithOffset(pos, k, i, l);
                    if (!this.isFree(reader, blockPos)) {
                        return i - 2;
                    }
                }
            }
        }
        return height;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void setBlock(IWorldWriter worldWriter, BlockPos pos, BlockState state) {
        this.setBlockKnownShape(worldWriter, pos, state);
    }

    @Override
    @ParametersAreNonnullByDefault
    public final boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos pos, OreTreeConfig config) {
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        MutableBoundingBox mutableboundingbox = MutableBoundingBox.getUnknownBox();
        boolean canPlace = this.doPlace(seedReader, seedReader, random, pos, set, set1, mutableboundingbox, config);
        if (mutableboundingbox.x0 <= mutableboundingbox.x1 && canPlace && !set.isEmpty()) {
            VoxelShapePart voxelshapepart = this.updateLeaves(seedReader, mutableboundingbox, set, set2);
            Template.updateShapeAtEdge(seedReader, 3, voxelshapepart, mutableboundingbox.x0, mutableboundingbox.y0, mutableboundingbox.z0);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShapePart updateLeaves(IWorld world, MutableBoundingBox boundingBox, Set<BlockPos> posSet, Set<BlockPos> blockPosSet) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(boundingBox.getXSpan(), boundingBox.getYSpan(), boundingBox.getZSpan());

        for (int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        BlockPos.Mutable tmpPos = new BlockPos.Mutable();

        for (BlockPos blockPos : Lists.newArrayList(blockPosSet)) {
            if (boundingBox.isInside(blockPos)) {
                voxelshapepart.setFull(blockPos.getX() - boundingBox.x0, blockPos.getY() - boundingBox.y0, blockPos.getZ() - boundingBox.z0, true, true);
            }
        }

        for (BlockPos blockPos1 : Lists.newArrayList(posSet)) {
            if (boundingBox.isInside(blockPos1)) {
                voxelshapepart.setFull(blockPos1.getX() - boundingBox.x0, blockPos1.getY() - boundingBox.y0, blockPos1.getZ() - boundingBox.z0, true, true);
            }

            for (Direction direction : Direction.values()) {
                tmpPos.setWithOffset(blockPos1, direction);
                if (!posSet.contains(tmpPos)) {
                    BlockState atPos = world.getBlockState(tmpPos);
                    if (atPos.hasProperty(BlockStateProperties.DISTANCE)) {
                        list.get(0).add(tmpPos.immutable());
                        this.setBlockKnownShape(world, tmpPos, atPos.setValue(BlockStateProperties.DISTANCE, 1));
                        if (boundingBox.isInside(tmpPos)) {
                            voxelshapepart.setFull(tmpPos.getX() - boundingBox.x0, tmpPos.getY() - boundingBox.y0, tmpPos.getZ() - boundingBox.z0, true, true);
                        }
                    }
                }
            }
        }

        for (int l = 1; l < 6; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for (BlockPos blockPos2 : set) {
                if (boundingBox.isInside(blockPos2)) {
                    voxelshapepart.setFull(blockPos2.getX() - boundingBox.x0, blockPos2.getY() - boundingBox.y0, blockPos2.getZ() - boundingBox.z0, true, true);
                }

                for (Direction direction1 : Direction.values()) {
                    tmpPos.setWithOffset(blockPos2, direction1);
                    if (!set.contains(tmpPos) && !set1.contains(tmpPos)) {
                        BlockState stateAt = world.getBlockState(tmpPos);
                        if (stateAt.hasProperty(BlockStateProperties.DISTANCE)) {
                            int k = stateAt.getValue(BlockStateProperties.DISTANCE);
                            if (k > l + 1) {
                                BlockState stateFromProp = stateAt.setValue(BlockStateProperties.DISTANCE, l + 1);
                                this.setBlockKnownShape(world, tmpPos, stateFromProp);
                                if (boundingBox.isInside(tmpPos)) {
                                    voxelshapepart.setFull(tmpPos.getX() - boundingBox.x0, tmpPos.getY() - boundingBox.y0, tmpPos.getZ() - boundingBox.z0, true, true);
                                }

                                set1.add(tmpPos.immutable());
                            }
                        }
                    }
                }
            }
        }

        return voxelshapepart;
    }
}
