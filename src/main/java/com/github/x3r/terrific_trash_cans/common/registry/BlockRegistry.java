package com.github.x3r.terrific_trash_cans.common.registry;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.common.block.EnergyTrashCanBlock;
import com.github.x3r.terrific_trash_cans.common.block.FluidTrashCanBlock;
import com.github.x3r.terrific_trash_cans.common.block.ItemTrashCanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TerrificTrashCans.MOD_ID);

    public static final RegistryObject<Block> ENERGY_TRASH_CAN = BLOCKS.register("energy_trash_can", EnergyTrashCanBlock::new);
    public static final RegistryObject<Block> FLUID_TRASH_CAN = BLOCKS.register("fluid_trash_can", FluidTrashCanBlock::new);
    public static final RegistryObject<Block> ITEM_TRASH_CAN = BLOCKS.register("item_trash_can", ItemTrashCanBlock::new);
}
