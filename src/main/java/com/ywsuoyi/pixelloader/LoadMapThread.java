package com.ywsuoyi.pixelloader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LiquidBlock;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadMapThread extends LoadingThread {


    public LoadMapThread(File file, UseOnContext context, int cc, int no, boolean fs) {
        super(file, context, cc, no, fs);
        this.blockList = Setting.mapBlocks;
    }

    @Override
    public void run() {
        try {
            BufferedImage read = ImageIO.read(file);
            int width = read.getWidth();
            int height = read.getHeight();
            int border = Math.max(width, height);
            double step = border / (128d * cc);
            int sx = Mth.floor((bp.getX() + 64.0D) / 128d) * 128 - 64;
            int sz = Mth.floor((bp.getZ() + 64.0D) / 128d) * 128 - 64;
            List<Integer> ylist = new ArrayList<>();
            List<Block> blist = new ArrayList<>();
            for (double x = read.getMinX(); x < width; x += step) {
                ylist.clear();
                blist.clear();
                int tmpy = 0;
                for (double y = read.getMinY(); y < height; y += step) {
                    if (!run) {
                        if (player != null)
                            player.displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.stop"), true);
                        Setting.threads.remove(no);
                        Setting.startNextThread();
                        return;
                    }
                    ColoredBlock block = CBlock(Setting.lt ? Setting.mapltColorBlockMap : Setting.mapColorBlockMap, CRGB(read.getRGB((int) x, (int) y)));
                    blist.add(block.block);
                    tmpy += block.y;
                    ylist.add(tmpy);
                }
                for (int i = 0; i < ylist.size(); i++) {
                    BlockPos pos = new BlockPos(sx + x / step, ylist.get(i) - Collections.min(ylist) + bp.getY(), sz + i);
                    if (blist.get(i) instanceof FallingBlock) {
                        world.setBlock(pos.offset(0, -1, 0), Blocks.DIRT.defaultBlockState(), 3);
                    }
                    world.setBlock(pos, blist.get(i).defaultBlockState(), 3);
                }
                this.message = ((int) x) + "/" + width;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        world.setBlock(bp.below(), Blocks.AIR.defaultBlockState(), 3);
        Setting.threads.remove(no);
        Setting.startNextThread();
        if (player != null)
            player.displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.finish"), true);
    }
}
