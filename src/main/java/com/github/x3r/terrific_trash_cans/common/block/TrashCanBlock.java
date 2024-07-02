package com.github.x3r.terrific_trash_cans.common.block;

import com.github.x3r.terrific_trash_cans.common.block_entity.TrashCanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public abstract class TrashCanBlock extends BaseEntityBlock {

    private static VoxelShape shape;

    public TrashCanBlock() {
        super(Properties.of().strength(1.5F, 3.0F).sound(SoundType.LODESTONE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return getOrCreateShape();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    protected VoxelShape getOrCreateShape() {
        if(shape == null) {
            VoxelShape s = Shapes.empty();
            s = Shapes.join(s, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.625, 0.8125), BooleanOp.OR);
            s = Shapes.join(s, Shapes.box(0.125, 0.625, 0.125, 0.875, 0.8125, 0.875), BooleanOp.OR);
            shape = s.optimize();
        }
        return shape;
    }
}
