package com.github.x3r.terrific_trash_cans.common.block;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TTCItemHandler extends ItemStackHandler {

    public static final String TAG_KEY = "ItemHandler";
    public TTCItemHandler(int size) {
        super(size);
    }

    public TTCItemHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public NonNullList<ItemStack> getItems() {
        return stacks;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt.getCompound(TAG_KEY));
    }
}
