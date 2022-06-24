package com.ywsuoyi.pixelloader;

import net.minecraft.world.level.block.Block;

public class ColoredBlock {
    int r, g, b, y = 0;
    Block block;

    public ColoredBlock(int r, int g, int b, Block block) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.block = block;
    }

    public ColoredBlock(int rgb, Block block, int y) {
        this.b = (rgb >> 16) & 0xff;
        this.g = (rgb >> 8) & 0xff;
        this.r = rgb & 0xff;
        this.block = block;
        this.y = y;
    }
}
