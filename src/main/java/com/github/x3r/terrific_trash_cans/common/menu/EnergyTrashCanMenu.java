package com.github.x3r.terrific_trash_cans.common.menu;

import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.registry.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EnergyTrashCanMenu extends SyncedMenu<EnergyTrashCanBlockEntity> {
    private final Container container;
    public EnergyTrashCanMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, (EnergyTrashCanBlockEntity) SyncedMenu.getBufferBlockEntity(inventory.player.level(), buf));
    }

    public EnergyTrashCanMenu(int pContainerId, Inventory inventory, @Nullable EnergyTrashCanBlockEntity blockEntity) {
        super(MenuTypeRegistry.ENERGY_TRASH_CAN.get(), pContainerId, inventory, blockEntity);
        this.container = blockEntity;
        checkContainerSize(container, 0);

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
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.container.stopOpen(pPlayer);
    }
}
