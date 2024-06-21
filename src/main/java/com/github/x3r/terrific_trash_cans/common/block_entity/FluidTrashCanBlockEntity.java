package com.github.x3r.terrific_trash_cans.common.block_entity;

import com.github.x3r.terrific_trash_cans.common.block.FluidTrashCanBlock;
import com.github.x3r.terrific_trash_cans.common.block.TTCEnergyStorage;
import com.github.x3r.terrific_trash_cans.common.block.TTCFluidHandler;
import com.github.x3r.terrific_trash_cans.common.block.TTCItemHandler;
import com.github.x3r.terrific_trash_cans.common.menu.FluidTrashCanMenu;
import com.github.x3r.terrific_trash_cans.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidTrashCanBlockEntity extends TrashCanBlockEntity {

    private final LazyOptional<TTCFluidHandler> fluidHandlerOptional = LazyOptional.of(() -> new TTCFluidHandler(
            new TTCFluidHandler.FluidTank[]{new TTCFluidHandler.FluidTank(Integer.MAX_VALUE)}));
    private final LazyOptional<TTCItemHandler> itemHandlerOptional = LazyOptional.of(() -> new TTCItemHandler(1));


    public FluidTrashCanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FLUID_TRASH_CAN.get(), pPos, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, FluidTrashCanBlockEntity pBlockEntity) {
        ItemStack stack = pBlockEntity.getItems().get(0);
        if(!stack.isEmpty()) {
            Optional<IFluidHandlerItem> handler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
            if(FluidTrashCanBlock.isBucketItem(stack)) {
                pBlockEntity.getItems().set(0, Items.BUCKET.getDefaultInstance());
            }
            if(FluidTrashCanBlock.isBottleItem(stack)) {
                pBlockEntity.getItems().set(0, Items.GLASS_BOTTLE.getDefaultInstance());
            }
            if(handler.isPresent() ) {
                while (handler.get().drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).getAmount() > 0) {
                    handler.get().drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
        pBlockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(fluidHandler -> {
            for (int i = 0; i < fluidHandler.getTanks(); i++) {
                if(fluidHandler.getFluidInTank(i).getAmount() > 0) {
                    fluidHandler.getFluidInTank(i).setAmount(0);
                }
            }
        });
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        if(itemHandlerOptional.isPresent()) {
            return itemHandlerOptional.orElseThrow(IllegalStateException::new).getItems();
        }
        return NonNullList.of(ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        fluidHandlerOptional.ifPresent(handler -> handler.deserializeNBT(pTag));
        itemHandlerOptional.ifPresent(handler -> handler.deserializeNBT(pTag));
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        fluidHandlerOptional.ifPresent(handler -> tag.put(TTCFluidHandler.TAG_KEY, handler.serializeNBT()));
        itemHandlerOptional.ifPresent(handler -> tag.put(TTCEnergyStorage.TAG_KEY, handler.serializeNBT()));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(ForgeCapabilities.FLUID_HANDLER)) {
            return fluidHandlerOptional.cast();
        }
        if(cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return itemHandlerOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandlerOptional.invalidate();
        itemHandlerOptional.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.terrific_trash_cans.fluid_trash_can");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FluidTrashCanMenu(i, inventory, this);
    }
}
