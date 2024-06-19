package com.github.x3r.terrific_trash_cans.client.screen;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import com.github.x3r.terrific_trash_cans.common.menu.EnergyTrashCanMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnergyTrashCanScreen extends TTCScreen<EnergyTrashCanMenu> {
    private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation(TerrificTrashCans.MOD_ID, "textures/gui/container/widgets.png");

    public EnergyTrashCanScreen(EnergyTrashCanMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new TTCWidgets.InformationWidget(leftPos + 158, topPos + 6, "energy_trash_can"));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float v, int i, int i1) {
        graphics.pose().pushPose();
        graphics.blit(CONTAINER_LOCATION, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.pose().popPose();
    }


}
