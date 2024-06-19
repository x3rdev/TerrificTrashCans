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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class EnergyTrashCanBlockEntity extends TrashCanBlockEntity {

    private final LazyOptional<TTCEnergyStorage> energyStorageOptional = LazyOptional.of(() -> new TTCEnergyStorage(Integer.MAX_VALUE, 0, Integer.MAX_VALUE));

    public EnergyTrashCanBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.ENERGY_TRASH_CAN.get(), pPos, pBlockState);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return NonNullList.of(ItemStack.EMPTY);
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

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        energyStorageOptional.ifPresent(energyStorage -> energyStorage.deserializeNBT(pTag));
    }
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        energyStorageOptional.ifPresent(energyStorage -> tag.put(TTCEnergyStorage.TAG_KEY, energyStorage.serializeNBT()));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap.equals(ForgeCapabilities.ENERGY)) {
            return energyStorageOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyStorageOptional.invalidate();
    }
}
