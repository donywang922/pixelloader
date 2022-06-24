package com.ywsuoyi.pixelloader;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class ColoredBlockLoader extends Item {

    public ColoredBlockLoader(Properties settings) {
        super(settings);
    }

    public void load(Level world, BlockPos pos, Player player) {
        Map<Block, String> blockStringMap = Maps.newHashMap();
        Setting.colorBlockMap.clear();
        Setting.mapColorBlockMap.clear();
        Setting.mapltColorBlockMap.clear();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        for (Block b : ForgeRegistries.BLOCKS) {
            boolean bool = Block.isShapeFullBlock(b.defaultBlockState().getCollisionShape(world, pos));
            bool |= b instanceof LiquidBlock;
            bool &= !BlockTags.SHULKER_BOXES.contains(b);
            for (ItemStack itemStack : Setting.banItem) {
                if (itemStack.getItem() instanceof BucketItem && b instanceof LiquidBlock) {
                    if (((BucketItem) itemStack.getItem()).getFluid() == ((LiquidBlock) b).getFluid()) {
                        bool = false;
                        break;
                    }
                }
                if (itemStack.getItem() == b.asItem()) {
                    bool = false;
                    break;
                }
            }
            if (bool) {
                if (b.defaultBlockState().getMapColor(world,BlockPos.ZERO).col != 0) {
                    Setting.mapBlocks.add(new ColoredBlock(b.defaultBlockState().getMapColor(world,BlockPos.ZERO).calculateRGBColor(MaterialColor.Brightness.LOW), b, -1));
                    Setting.mapBlocks.add(new ColoredBlock(b.defaultBlockState().getMapColor(world,BlockPos.ZERO).calculateRGBColor(MaterialColor.Brightness.NORMAL), b, 0));
                    Setting.mapBlocks.add(new ColoredBlock(b.defaultBlockState().getMapColor(world,BlockPos.ZERO).calculateRGBColor(MaterialColor.Brightness.HIGH), b, 1));
                }

                if (ItemBlockRenderTypes.canRenderInLayer(b.defaultBlockState(),RenderType.solid())) {
                    //block render
                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(b);
                    if (id != null) {
                        ResourceLocation id2 = new ResourceLocation(id.getNamespace(), "blockstates/" + id.getPath() + ".json");
                        try {
                            List<Resource> allResources = resourceManager.getResources(id2);
                            Resource r = allResources.get(allResources.size() - 1);
                            JsonObject json = JsonParser.parseReader(new InputStreamReader(r.getInputStream())).getAsJsonObject();
                            if (json.has("variants")) {
                                String s2 = "";
                                boolean b2 = true;
                                for (Map.Entry<String, JsonElement> variants : json.getAsJsonObject("variants").entrySet()) {
                                    String s = variants.getValue().isJsonArray() ?
                                            variants.getValue().getAsJsonArray().get(0).getAsJsonObject().get("model").getAsString() :
                                            variants.getValue().getAsJsonObject().get("model").getAsString();
                                    if (s2.equals(""))
                                        s2 = s;
                                    else
                                        b2 &= s.equals(s2);
                                }
                                if (b2) {
                                    //state
                                    String[] s3 = decompose(s2);
                                    ResourceLocation id3 = new ResourceLocation(s3[0], "models/" + s3[1] + ".json");
                                    List<Resource> allResources1 = resourceManager.getResources(id3);
                                    Resource r1 = allResources1.get(allResources1.size() - 1);
                                    JsonObject json1 = JsonParser.parseReader(new InputStreamReader(r1.getInputStream())).getAsJsonObject();
                                    String s1 = "";
                                    boolean b1 = true;
                                    if (json1.has("textures")) {
                                        for (Map.Entry<String, JsonElement> j : json1.getAsJsonObject("textures").entrySet()) {
                                            if (s1.equals("")) s1 = j.getValue().getAsString();
                                            else b1 &= j.getValue().getAsString().equals(s1);
                                        }
                                        if (b1) {
//                                  //texture
                                            blockStringMap.put(b, s1);
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        Setting.coloredBlocks.clear();
        blockStringMap.forEach((block, s) -> {
            try {
                String[] s1 = decompose(s);
                if (!resourceManager.hasResource(new ResourceLocation(s1[0], "textures/" + s1[1] + ".png.mcmeta"))) {
                    List<Resource > allResources = resourceManager.getResources(new ResourceLocation(s1[0], "textures/" + s1[1] + ".png"));
                    Resource resource = allResources.get(allResources.size() - 1);
                    BufferedImage read = ImageIO.read(resource.getInputStream());
                    int width = read.getWidth();
                    int height = read.getHeight();
                    long sumr = 0, sumg = 0, sumb = 0;
                    boolean noA = true;
                    for (int y = read.getMinY(); y < height; y++) {
                        for (int x = read.getMinX(); x < width; x++) {
                            Color pixel = new Color(read.getRGB(x, y), true);
                            sumr += pixel.getRed();
                            sumg += pixel.getGreen();
                            sumb += pixel.getBlue();
                            noA &= pixel.getAlpha() == 255;
                        }
                    }
                    if (noA) {
                        int num = width * height;
                        Setting.coloredBlocks.add(new ColoredBlock((int) sumr / num, (int) sumg / num, (int) sumb / num, block));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (player != null)
            player.displayClientMessage(new TranslatableComponent("pixelLoader.colored_block.loaded"), true);
        Setting.ed = true;
    }


    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, @Nonnull Player player, @Nonnull InteractionHand handIn) {
        if (!worldIn.isClientSide)
            if (player.isShiftKeyDown()) {
                load(worldIn, player.getOnPos(), player);
            } else {
                player.openMenu(new SimpleMenuProvider(
                        (id, inventory, playerIn) -> ChestMenu.sixRows(id, inventory, new banBlockInv()),
                        Setting.banItemScreen
                ));
            }
        return InteractionResultHolder.success(player.getItemInHand(handIn));
    }


    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            if (context.getPlayer() != null)
                if (context.getPlayer().isShiftKeyDown()) {
                    if (Setting.ed) {
                        Setting.coloredBlocks.forEach(coloredBlock -> {
                            context.getLevel().setBlock(context.getClickedPos().offset(coloredBlock.r / 6, coloredBlock.g / 6 - 1, coloredBlock.b / 6), Blocks.GLASS.defaultBlockState(),3);
                            context.getLevel().setBlock(context.getClickedPos().offset(coloredBlock.r / 6, coloredBlock.g / 6, coloredBlock.b / 6), coloredBlock.block.defaultBlockState(),3);
                        });
                    } else load(context.getLevel(), context.getClickedPos(), context.getPlayer());
                } else {
                    context.getPlayer().openMenu(new SimpleMenuProvider(
                            (id, inventory, playerIn) -> ChestMenu.sixRows(id, inventory, new banBlockInv()),
                            Setting.banItemScreen
                    ));
                }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag context) {
        tooltip.add(new TranslatableComponent("pixelLoader.colored_block.tip"));
        if (Setting.ed) {
            tooltip.add(new TranslatableComponent("pixelLoader.colored_block.place"));
            tooltip.add(new TranslatableComponent("pixelLoader.colored_block.reload"));
        } else {
            tooltip.add(new TranslatableComponent("pixelLoader.colored_block.load"));
        }
        super.appendHoverText(stack, world, tooltip, context);
    }

    public static String[] decompose(String resourceName) {
        String[] astring = new String[]{"minecraft", resourceName};
        int i = resourceName.indexOf(':');
        if (i >= 0) {
            astring[1] = resourceName.substring(i + 1);
            if (i >= 1) {
                astring[0] = resourceName.substring(0, i);
            }
        }

        return astring;
    }
}
