package com.ywsuoyi.pixelloader;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;

public class SettingScreen extends Screen {
    public Button fs, cutout;

    public SettingScreen() {
        super(new TranslatableComponent("pixelLoader.screen"));
    }

    @Override
    public void init() {
        cutout = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 - 60, 200, 20, new TranslatableComponent("pixelLoader.screen.cutout." + Setting.cutout), p_onPress_1_ -> {
            Setting.cutout++;
            Setting.cutout = Setting.cutout > 3 ? 0 : Setting.cutout;
            cutout.setMessage(new TranslatableComponent("pixelLoader.screen.cutout." + Setting.cutout));
        }));
        fs = this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 - 36, 200, 20, new TranslatableComponent("pixelLoader.screen.fs." + Setting.fs), p_onPress_1_ -> {
            Setting.fs = !Setting.fs;
            fs.setMessage(new TranslatableComponent("pixelLoader.screen.fs." + Setting.fs));
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 + 36, 200, 20, new TranslatableComponent("pixelLoader.screen.stop"), p_onPress_1_ -> Setting.stopAllThread()));
        this.addRenderableWidget(new Button(this.width / 2 - 100, height / 2 + 60, 200, 20, new TranslatableComponent("pixelLoader.screen.close"), p_onPress_1_ -> this.onClose()));
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
