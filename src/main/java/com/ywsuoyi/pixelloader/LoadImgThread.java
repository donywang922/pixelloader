package com.ywsuoyi.pixelloader;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class LoadImgThread extends LoadingThread {

    public boolean pm;

    public LoadImgThread(File file, UseOnContext context, int cc, boolean pm, int no, boolean fs) {
        super(file, context, cc, no, fs);
        this.pm = pm;
        this.blockList = Setting.coloredBlocks;
    }

    @Override
    public void run() {
        try {
            BufferedImage read = ImageIO.read(file);
            int width = read.getWidth();
            int height = read.getHeight();
            for (int y = height - 1; y >= read.getMinY(); y -= cc) {
                for (int x = read.getMinX(); x < width; x += cc) {
                    if (!run) {
                        if (player != null)
                            player.displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.stop"), true);
                        Setting.threads.remove(no);
                        Setting.startNextThread();
                        return;
                    }
                    ColoredBlock block = CBlock(Setting.colorBlockMap, CRGB(read.getRGB(x, y)));

                    world.setBlock(
                            bp.offset((pm ? width - x + cc : x + cc) / cc, pm ? 0 : (height - y) / cc, pm ? (height - y) / cc : 0),
                            block.block.defaultBlockState(),3);
                }
                this.message = (height - y) + "/" + height;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setBlock(bp, Blocks.AIR.defaultBlockState(),3);
        Setting.threads.remove(no);
        Setting.startNextThread();
        if (player != null)
            player.displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.finish"), true);
    }
}
