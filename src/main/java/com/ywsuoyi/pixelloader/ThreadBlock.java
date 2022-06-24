package com.ywsuoyi.pixelloader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ThreadBlock extends Block {
    public static final IntegerProperty threadNO = IntegerProperty.create("threadno", 0, 16);

    public ThreadBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(threadNO, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(threadNO);
        super.createBlockStateDefinition(p_49915_);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Setting.stopThread(state.getValue(threadNO));
        return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
    }



    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand handIn, @Nonnull BlockHitResult hit) {
        if (Setting.threads.containsKey(state.getValue(threadNO)))
            player.displayClientMessage(Component.nullToEmpty(Setting.threads.get(state.getValue(threadNO)).message), true);
        return InteractionResult.SUCCESS;
    }


    @Override
    public void stepOn(@Nonnull Level world, @Nonnull BlockPos pos, @NotNull BlockState blockState, @Nonnull Entity entity) {
        if (Setting.threads.containsKey(world.getBlockState(pos).getValue(threadNO)))
            if (entity instanceof Player)
                ((Player) entity).displayClientMessage(
                        Component.nullToEmpty(Setting.threads.get(
                                world.getBlockState(pos).getValue(threadNO)).message), true);
        super.stepOn(world, pos, blockState, entity);
    }

}
