package com.ywsuoyi.pixelloader;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pixelloader")
public class pixelloader {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "pixelloader");
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "pixelloader");

    public static final RegistryObject<Item> coloredblockloader = ITEMS.register("coloredblockloader", () -> new ColoredBlockLoader(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> imgloader = ITEMS.register("imgloader", () -> new ImgLoader(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> maploader = ITEMS.register("maploader", () -> new MapLoader(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Block> threadblock = BLOCKS.register("threadblock", () -> new ThreadBlock(BlockBehaviour.Properties.of(Material.STONE)));

    public pixelloader() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        if (!Setting.imgFolder.exists())
            Setting.imgFolder.mkdir();
        Setting.banItem.set(0, new ItemStack(Items.TUBE_CORAL_BLOCK));
        Setting.banItem.set(1, new ItemStack(Items.BRAIN_CORAL_BLOCK));
        Setting.banItem.set(2, new ItemStack(Items.BUBBLE_CORAL_BLOCK));
        Setting.banItem.set(3, new ItemStack(Items.FIRE_CORAL_BLOCK));
        Setting.banItem.set(4, new ItemStack(Items.HORN_CORAL_BLOCK));
        Setting.banItem.set(5, new ItemStack(Items.BEDROCK));
        Setting.banItem.set(6, new ItemStack(Items.COPPER_BLOCK));
        Setting.banItem.set(7, new ItemStack(Items.CUT_COPPER));
        Setting.banItem.set(8, new ItemStack(Items.EXPOSED_COPPER));
        Setting.banItem.set(9, new ItemStack(Items.EXPOSED_CUT_COPPER));
        Setting.banItem.set(10, new ItemStack(Items.WEATHERED_COPPER));
        Setting.banItem.set(11, new ItemStack(Items.WEATHERED_CUT_COPPER));
        Setting.banItem.set(12, new ItemStack(Items.INFESTED_COBBLESTONE));
        Setting.banItem.set(13, new ItemStack(Items.INFESTED_CHISELED_STONE_BRICKS));
        Setting.banItem.set(14, new ItemStack(Items.INFESTED_CRACKED_STONE_BRICKS));
        Setting.banItem.set(15, new ItemStack(Items.INFESTED_DEEPSLATE));
        Setting.banItem.set(16, new ItemStack(Items.INFESTED_STONE));
        Setting.banItem.set(17, new ItemStack(Items.INFESTED_MOSSY_STONE_BRICKS));
        Setting.banItem.set(18, new ItemStack(Items.INFESTED_STONE_BRICKS));
        Setting.banItem.set(19, new ItemStack(Items.BUDDING_AMETHYST));
    }

    @SubscribeEvent
    public void ServerStop(ServerStoppingEvent event) {
        Setting.threads.forEach((integer, loadingThread) -> loadingThread.ForceStop());
    }

}
