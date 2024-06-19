package com.github.x3r.terrific_trash_cans;

import com.github.x3r.terrific_trash_cans.common.registry.BlockEntityRegistry;
import com.github.x3r.terrific_trash_cans.common.registry.BlockItemRegistry;
import com.github.x3r.terrific_trash_cans.common.registry.BlockRegistry;
import com.github.x3r.terrific_trash_cans.common.registry.MenuTypeRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TerrificTrashCans.MOD_ID)
public class TerrificTrashCans {

    public static final String MOD_ID = "terrific_trash_cans";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TerrificTrashCans() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        BlockItemRegistry.BLOCK_ITEMS.register(modEventBus);
        BlockItemRegistry.ModItemTab.CREATIVE_MODE_TABS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        MenuTypeRegistry.MENUS.register(modEventBus);
    }
}
