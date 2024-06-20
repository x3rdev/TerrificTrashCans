package com.github.x3r.terrific_trash_cans.client.screen;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.common.menu.EnergyTrashCanMenu;
import com.github.x3r.terrific_trash_cans.common.menu.ItemTrashCanMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ItemTrashCanScreen extends TTCScreen<ItemTrashCanMenu> {

    private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation(TerrificTrashCans.MOD_ID, "textures/gui/container/trash_can.png");

    public ItemTrashCanScreen(ItemTrashCanMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.pose().pushPose();
        guiGraphics.blit(CONTAINER_LOCATION, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.pose().popPose();
    }
}
