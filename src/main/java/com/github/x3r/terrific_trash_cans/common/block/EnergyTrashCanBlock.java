package com.github.x3r.terrific_trash_cans.common.block;

import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.block_entity.FluidTrashCanBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class EnergyTrashCanBlock extends TrashCanBlock {

    public EnergyTrashCanBlock() {
        super(Properties.of());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof EnergyTrashCanBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (MenuProvider) blockentity, buf -> buf.writeBlockPos(pPos));
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnergyTrashCanBlockEntity(blockPos, blockState);
    }
}
