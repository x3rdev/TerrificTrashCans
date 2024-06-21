package com.github.x3r.terrific_trash_cans.common.block_entity;

import com.github.x3r.terrific_trash_cans.common.block.TTCEnergyStorage;
import com.github.x3r.terrific_trash_cans.common.block.TTCItemHandler;
import com.github.x3r.terrific_trash_cans.common.menu.EnergyTrashCanMenu;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class EnergyTrashCanBlockEntity extends TrashCanBlockEntity {

    private final LazyOptional<TTCEnergyStorage> energyStorageOptional = LazyOptional.of(() -> new TTCEnergyStorage(Integer.MAX_VALUE, 0, Integer.MAX_VALUE));
    private final LazyOptional<TTCItemHandler> itemHandlerOptional = LazyOptional.of(() -> new TTCItemHandler(1));

    public EnergyTrashCanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.ENERGY_TRASH_CAN.get(), pPos, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, EnergyTrashCanBlockEntity pBlockEntity) {
        ItemStack itemStack = pBlockEntity.getItems().get(0);
        if(!itemStack.isEmpty()) {
            itemStack.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> {
                energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
            });
        }
        pBlockEntity.getCapability(ForgeCapabilities.ENERGY).ifPresent(energyStorage -> {
            if(energyStorage.getEnergyStored() > 0) {
                energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
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
        energyStorageOptional.ifPresent(energyStorage -> energyStorage.deserializeNBT(pTag));
        itemHandlerOptional.ifPresent(itemHandler -> itemHandler.deserializeNBT(pTag));
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        energyStorageOptional.ifPresent(energyStorage -> tag.put(TTCEnergyStorage.TAG_KEY, energyStorage.serializeNBT()));
        itemHandlerOptional.ifPresent(itemHandler -> tag.put(TTCItemHandler.TAG_KEY, itemHandler.serializeNBT()));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(ForgeCapabilities.ENERGY)) {
            return energyStorageOptional.cast();
        }
        if(cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return itemHandlerOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyStorageOptional.invalidate();
        itemHandlerOptional.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.terrific_trash_cans.energy_trash_can");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyTrashCanMenu(i, inventory, this);
    }
}
