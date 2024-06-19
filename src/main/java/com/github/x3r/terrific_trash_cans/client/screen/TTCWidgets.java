package com.github.x3r.terrific_trash_cans.client.screen;

import com.github.x3r.terrific_trash_cans.TerrificTrashCans;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TTCWidgets {

    public static class InformationWidget extends ImageButton {

        private static final ResourceLocation INFO_WIDGET = new ResourceLocation(TerrificTrashCans.MOD_ID, "textures/gui/container/widgets.png");

        public InformationWidget(int pX, int pY, String machineId) {
            super(pX, pY, 9, 9, 0, 0, 9, INFO_WIDGET, pButton -> {});
            this.setTooltip(Tooltip.create(Component.translatable("gui.terrific_trash_cans.info." + machineId)));
        }

        @Override
        public void onPress() {
        }
    }
}
