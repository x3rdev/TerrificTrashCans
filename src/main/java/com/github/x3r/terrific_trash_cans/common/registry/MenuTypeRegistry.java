package com.github.x3r.terrific_trash_cans.common.registry;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.common.block_entity.EnergyTrashCanBlockEntity;
import com.github.x3r.terrific_trash_cans.common.menu.EnergyTrashCanMenu;
import com.github.x3r.terrific_trash_cans.common.menu.FluidTrashCanMenu;
import com.github.x3r.terrific_trash_cans.common.menu.ItemTrashCanMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeRegistry {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TerrificTrashCans.MOD_ID);

    public static final RegistryObject<MenuType<EnergyTrashCanMenu>> ENERGY_TRASH_CAN = MENUS.register("energy_trash_can",
            () -> IForgeMenuType.create(EnergyTrashCanMenu::new));
    public static final RegistryObject<MenuType<FluidTrashCanMenu>> FLUID_TRASH_CAN = MENUS.register("fluid_trash_can",
            () -> IForgeMenuType.create(FluidTrashCanMenu::new));
    public static final RegistryObject<MenuType<ItemTrashCanMenu>> ITEM_TRASH_CAN = MENUS.register("item_trash_can",
            () -> IForgeMenuType.create(ItemTrashCanMenu::new));
}
