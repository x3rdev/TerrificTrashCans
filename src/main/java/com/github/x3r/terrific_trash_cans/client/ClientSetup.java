package com.github.x3r.terrific_trash_cans.client;


import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.client.screen.EnergyTrashCanScreen;
import com.github.x3r.terrific_trash_cans.client.screen.FluidTrashCanScreen;
import com.github.x3r.terrific_trash_cans.client.screen.ItemTrashCanScreen;
import com.github.x3r.terrific_trash_cans.common.registry.MenuTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TerrificTrashCans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {

    private ClientSetup(){}

    @SubscribeEvent
    public static void setup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuTypeRegistry.ENERGY_TRASH_CAN.get(), EnergyTrashCanScreen::new);
            MenuScreens.register(MenuTypeRegistry.FLUID_TRASH_CAN.get(), FluidTrashCanScreen::new);
            MenuScreens.register(MenuTypeRegistry.ITEM_TRASH_CAN.get(), ItemTrashCanScreen::new);
        });
    }
}
