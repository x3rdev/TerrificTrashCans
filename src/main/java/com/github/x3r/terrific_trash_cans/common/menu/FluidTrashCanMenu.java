package com.github.x3r.terrific_trash_cans.common.menu;

import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.block_entity.FluidTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.registry.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class FluidTrashCanMenu extends SyncedMenu<FluidTrashCanBlockEntity> {

    private final Container container;
    public FluidTrashCanMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, (FluidTrashCanBlockEntity) SyncedMenu.getBufferBlockEntity(inventory.player.level(), buf));
    }

    public FluidTrashCanMenu(int pContainerId, Inventory inventory, @Nullable FluidTrashCanBlockEntity blockEntity) {
        super(MenuTypeRegistry.FLUID_TRASH_CAN.get(), pContainerId, inventory, blockEntity);
        this.container = blockEntity;
        checkContainerSize(container, 1);
        this.addSlot(new Slot(container, 0, 80, 30){
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return super.mayPlace(pStack);
            }
        });

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }
    }
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack $$4 = slot.getItem();
            $$2 = $$4.copy();
            if (index < 1) {
                if (!this.moveItemStackTo($$4, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo($$4, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if ($$4.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, $$4);
        }

        return $$2;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity((BlockEntity) container, player);
    }
}
