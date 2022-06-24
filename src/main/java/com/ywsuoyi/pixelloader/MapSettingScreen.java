package com.ywsuoyi.pixelloader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;

public class MapSettingScreen extends SettingScreen {
    public Button mapsize, lt;

    @Override
    public void init() {
        super.init();
        mapsize = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 - 12, 200, 20, new TranslatableComponent("pixelLoader.screen.mapsize", Setting.mapSize), p_onPress_1_ -> {
            Setting.mapSize++;
            Setting.mapSize = Setting.mapSize > 8 ? 1 : Setting.mapSize;
            mapsize.setMessage(new TranslatableComponent("pixelLoader.screen.mapsize", Setting.mapSize));
        }));
        lt = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 + 12, 200, 20, new TranslatableComponent("pixelLoader.screen.lt." + Setting.lt), p_onPress_1_ -> {
            Setting.lt = !Setting.lt;
            lt.setMessage(new TranslatableComponent("pixelLoader.screen.lt." + Setting.lt));
        }));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
