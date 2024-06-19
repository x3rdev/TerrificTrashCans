package com.github.x3r.terrific_trash_cans.common.registry;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockItemRegistry  {

    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TerrificTrashCans.MOD_ID);

    public static final RegistryObject<Item> ENERGY_TRASH_CAN = BLOCK_ITEMS.register("energy_trash_can", () -> new BlockItem(BlockRegistry.ENERGY_TRASH_CAN.get(), new Item.Properties()));
    public static final RegistryObject<Item> FLUID_TRASH_CAN = BLOCK_ITEMS.register("fluid_trash_can", () -> new BlockItem(BlockRegistry.FLUID_TRASH_CAN.get(), new Item.Properties()));
    public static final RegistryObject<Item> ITEM_TRASH_CAN = BLOCK_ITEMS.register("item_trash_can", () -> new BlockItem(BlockRegistry.ITEM_TRASH_CAN.get(), new Item.Properties()));

    public static class ModItemTab {

        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerrificTrashCans.MOD_ID);

        public static final RegistryObject<CreativeModeTab> ITEM_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                .icon(Items.NAME_TAG::getDefaultInstance)
                .title(Component.literal("itemGroup." + TerrificTrashCans.MOD_ID))
                .displayItems((displayParameters, output) -> BlockItemRegistry.BLOCK_ITEMS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                .build());
    }
}
