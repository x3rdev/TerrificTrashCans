package com.github.x3r.terrific_trash_cans.common.registry;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TerrificTrashCans.MOD_ID);

    public static final RegistryObject<BlockEntityType<?>> ENERGY_TRASH_CAN = BLOCK_ENTITY_TYPES.register("energy_trash_can",
            () -> BlockEntityType.Builder.of(EnergyTrashCanBlockEntity::new, BlockRegistry.ENERGY_TRASH_CAN.get()).build(null));
}
