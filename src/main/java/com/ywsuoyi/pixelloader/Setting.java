package com.ywsuoyi.pixelloader;

import com.google.common.collect.Maps;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Setting {
    public static final ColoredBlock air = new ColoredBlock(0, Blocks.AIR, 0);

    public static final Map<Integer, LoadingThread> threads = new HashMap<>();

    public static File imgFolder = new File("./img");
    public static final List<File> imglist = new ArrayList<>();
    public static int index = 0;

    public static final BaseComponent banItemScreen = new TranslatableComponent("pixelLoader.banScreen");
    public static final NonNullList<ItemStack> banItem = NonNullList.withSize(54, ItemStack.EMPTY);

    public static final NonNullList<ColoredBlock> coloredBlocks = NonNullList.create();
    public static final NonNullList<ColoredBlock> mapBlocks = NonNullList.create();
    public static boolean ed = false;

    public static final Map<Integer, ColoredBlock> colorBlockMap = Maps.newHashMap();
    public static final Map<Integer, ColoredBlock> mapColorBlockMap = Maps.newHashMap();
    public static final Map<Integer, ColoredBlock> mapltColorBlockMap = Maps.newHashMap();

    public static boolean fs = true;
    public static int cutout = 0;

    public static int mapSize = 1;
    public static boolean lt = true;

    public static int imgSize = 8;
    public static boolean pm = true;


    public static File getImg() {
        return imglist.get(index);
    }

    public static void addindex(Player player) {
        try {
            index++;
            if (index > imglist.size() - 1) {
                index = 0;
                imglist.clear();
                Files.list(Paths.get("img/"))
                        .filter(f ->
                                !Files.isDirectory(f) && (
                                        f.getFileName().toString().endsWith(".jpg") ||
                                                f.getFileName().toString().endsWith(".jpeg") ||
                                                f.getFileName().toString().endsWith(".png")))
                        .forEach(path -> {
                            if (Files.isDirectory(path))
                                return;
                            imglist.add(path.toFile());
                        });
                if (imglist.isEmpty()) {
                    if (player != null)
                        player.displayClientMessage(new TranslatableComponent("pixelLoader.fileNotFind"), true);
                    return;
                }
            }
            if (player != null)
                player.displayClientMessage(new TranslatableComponent("pixelLoader.selectFile", imglist.get(index).getName()), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startNextThread() {
        boolean run = false;
        for (Map.Entry<Integer, LoadingThread> entry : threads.entrySet()) {
            run |= entry.getValue().run;
        }
        if (!run && threads.size() > 0) {
            Map.Entry<Integer, LoadingThread> next = threads.entrySet().iterator().next();
            try {
                next.getValue().start();
            } catch (Exception e) {
                threads.remove(next.getKey());
            }
        }
    }

    public static void stopThread(int No) {
        if (threads.containsKey(No)) {
            threads.get(No).ForceStop();
            threads.remove(No);
        }
    }

    public static void stopAllThread() {
        threads.forEach((integer, loadingThread) -> loadingThread.ForceStop());
    }

}
