package com.github.x3r.terrific_trash_cans.common.block;

import com.github.x3r.terrific_trash_cans.common.block_entity.FluidTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.block_entity.ItemTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidTrashCanBlock extends TrashCanBlock {

    public FluidTrashCanBlock() {
        super(Properties.of());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof FluidTrashCanBlockEntity) {
                ItemStack stack = pPlayer.getItemInHand(pHand);
                Optional<IFluidHandlerItem> handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                if(isBucketItem(stack)) {
                    pPlayer.setItemInHand(pHand, Items.BUCKET.getDefaultInstance());
                    return InteractionResult.CONSUME;
                }
                if(isBottleItem(stack)) {
                    pPlayer.setItemInHand(pHand, Items.GLASS_BOTTLE.getDefaultInstance());
                    return InteractionResult.CONSUME;
                }
                if(handler.isPresent() ) {
                    while (handler.get().drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).getAmount() > 0) {
                        handler.get().drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                    }
                    return InteractionResult.CONSUME;
                }
                NetworkHooks.openScreen((ServerPlayer) pPlayer, (MenuProvider) blockentity, buf -> buf.writeBlockPos(pPos));
            }
            return InteractionResult.CONSUME;
        }
    }

    public static boolean isBucketItem(ItemStack stack) {
        return stack.getItem() instanceof BucketItem || stack.getItem() instanceof SolidBucketItem || stack.getItem() instanceof MilkBucketItem;
    }

    public static boolean isBottleItem(ItemStack stack) {
       return stack.getItem() instanceof PotionItem || stack.getItem() instanceof HoneyBottleItem || stack.getItem() instanceof ExperienceBottleItem;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, BlockEntityRegistry.FLUID_TRASH_CAN.get(), FluidTrashCanBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidTrashCanBlockEntity(blockPos, blockState);
    }
}
