package com.github.x3r.terrific_trash_cans.common.block;

import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.block_entity.FluidTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.block_entity.ItemTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyTrashCanBlock extends TrashCanBlock {

    public EnergyTrashCanBlock() {
        super();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof EnergyTrashCanBlockEntity) {
                Optional<IEnergyStorage> handler = pPlayer.getItemInHand(pHand).getCapability(ForgeCapabilities.ENERGY).resolve();
                if(!pPlayer.isCrouching()) {
                    if (handler.isPresent()) {
                        while (handler.get().extractEnergy(Integer.MAX_VALUE, true) > 0) {
                            handler.get().extractEnergy(Integer.MAX_VALUE, false);
                        }
                        return InteractionResult.CONSUME;
                    }
                }
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (MenuProvider) blockentity, buf -> buf.writeBlockPos(pPos));
            }
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, BlockEntityRegistry.ENERGY_TRASH_CAN.get(), EnergyTrashCanBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnergyTrashCanBlockEntity(blockPos, blockState);
    }
}
