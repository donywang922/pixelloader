package com.ywsuoyi.pixelloader;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.io.File;
import java.util.Map;

public class LoadingThread extends Thread {
    public String message = "";
    public File file;
    public Player player;
    public BlockPos bp;
    public Level world;
    public boolean run = false;
    public boolean fs;
    public int cc;
    public int r = 0, g = 0, b = 0;
    public NonNullList<ColoredBlock> blockList;
    public int no;

    public LoadingThread(File file, UseOnContext context, int cc, int no, boolean fs) {
        this.file = file;
        this.player = context.getPlayer();
        this.bp = context.getClickedPos().above();
        this.world = context.getLevel();
        this.cc = cc;
        this.no = no;
        this.fs = fs;
    }

    public int CRGB(int rgb) {
        if (
                (Setting.cutout == 1 && ((rgb >> 16) & 0xFF) > 250 && ((rgb >> 8) & 0xFF) > 250 && (rgb & 0xFF) > 250) ||
                        (Setting.cutout == 2 && ((rgb >> 16) & 0xFF) < 5 && ((rgb >> 8) & 0xFF) < 5 && (rgb & 0xFF) < 5) ||
                        (Setting.cutout == 3 && ((rgb >> 24) & 0xFF) != 255)
        ) return 0;
        if (fs) {
            r = Math.abs(r) > 64 ? 0 : r;
            g = Math.abs(g) > 64 ? 0 : g;
            b = Math.abs(b) > 64 ? 0 : b;
            r += (rgb >> 16) & 0xFF;
            g += (rgb >> 8) & 0xFF;
            b += rgb & 0xFF;
        } else {
            r = (rgb >> 16) & 0xFF;
            g = (rgb >> 8) & 0xFF;
            b = rgb & 0xFF;
        }
        return ((Math.max(Math.min(r, 255), 0) & 0xFF) << 16) |
                ((Math.max(Math.min(g, 255), 0) & 0xFF) << 8) |
                ((Math.max(Math.min(b, 255), 0) & 0xFF)) | 0xFF000000;
    }

    public ColoredBlock CBlock(Map<Integer, ColoredBlock> blockMap, int rgb) {
        if (((rgb >> 24) & 0xFF) == 0) {
            return Setting.air;
        }
        ColoredBlock block;


        if (blockMap.containsKey(rgb)) {
            block = blockMap.get(rgb);
        } else {
            block = blockList.get(1);
            double d = Math.pow((block.r - r) * 0.30, 2) + Math.pow((block.g - g) * 0.59, 2) + Math.pow((block.b - b) * 0.11, 2);
            for (ColoredBlock coloredBlock : blockList) {
                if (Setting.lt || coloredBlock.y == 0) {
                    double t = Math.pow((coloredBlock.r - r) * 0.30, 2) + Math.pow((coloredBlock.g - g) * 0.59, 2) + Math.pow((coloredBlock.b - b) * 0.11, 2);
                    if (t < d) {
                        d = t;
                        block = coloredBlock;
                    }
                }
            }
            blockMap.put(rgb, block);
        }

        r -= block.r;
        g -= block.g;
        b -= block.b;
        return block;
    }


    @Override
    public synchronized void start() {
        run = true;
        if (player != null)
            player.displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.start"), true);
        super.start();
    }

    public void ForceStop() {
        this.run = false;
    }
}
