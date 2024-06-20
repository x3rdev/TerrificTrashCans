package com.github.x3r.terrific_trash_cans.common.block_entity;

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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class FluidTrashCanBlockEntity extends TrashCanBlockEntity {

    private final LazyOptional<TTCFluidHandler> fluidHandlerOptional = LazyOptional.of(() -> new TTCFluidHandler(
            new TTCFluidHandler.FluidTank[]{new TTCFluidHandler.FluidTank(Integer.MAX_VALUE)}));
    private final LazyOptional<TTCItemHandler> itemHandlerOptional = LazyOptional.of(() -> new TTCItemHandler(1));


    public FluidTrashCanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.FLUID_TRASH_CAN.get(), pPos, pBlockState);
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
        fluidHandlerOptional.ifPresent(handler -> tag.put(TTCEnergyStorage.TAG_KEY, handler.serializeNBT()));
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
