package com.ywsuoyi.pixelloader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;

public class ImgSettingScreen extends SettingScreen {
    public Button imgsize, pm;


    @Override
    public void init() {
        super.init();
        imgsize = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 - 12, 200, 20, new TranslatableComponent("pixelLoader.screen.imgsize", Setting.imgSize), p_onPress_1_ -> {
            Setting.imgSize++;
            Setting.imgSize = Setting.imgSize > 8 ? 1 : Setting.imgSize;
            imgsize.setMessage(new TranslatableComponent("pixelLoader.screen.imgsize", Setting.imgSize));
        }));
        pm = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 + 12, 200, 20, new TranslatableComponent("pixelLoader.screen.pm." + Setting.pm), p_onPress_1_ -> {
            Setting.pm = !Setting.pm;
            pm.setMessage(new TranslatableComponent("pixelLoader.screen.pm." + Setting.pm));
        }));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
