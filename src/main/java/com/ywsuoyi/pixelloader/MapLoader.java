package com.ywsuoyi.pixelloader;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MapLoader extends Item {

    public MapLoader(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }


    @Override
    @Nonnull
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        if (!Setting.ed) {
            if (context.getPlayer() != null)
                context.getPlayer().displayClientMessage(new TranslatableComponent("pixelLoader.colored_block.needload"), true);
            return InteractionResult.FAIL;
        }
        if (!context.getLevel().isClientSide && context.getPlayer() != null) {
            if (!context.getPlayer().isShiftKeyDown()) {
                Setting.addindex(context.getPlayer());
            } else if (Setting.imglist.size() == 0) {
                context.getPlayer().displayClientMessage(new TranslatableComponent("pixelLoader.noFile"), true);
            } else {
                for (int i = 1; i <= 16; i++) {
                    if (!Setting.threads.containsKey(i)) {
                        LoadingThread thread = new LoadMapThread(Setting.getImg(), context, Setting.mapSize, i, Setting.fs);
                        Setting.threads.put(i, thread);
                        context.getLevel().setBlock(context.getClickedPos(), pixelloader.threadblock.get().defaultBlockState().setValue(ThreadBlock.threadNO, i),3);
                        Setting.startNextThread();
                        return InteractionResult.SUCCESS;
                    }
                }
                context.getPlayer().displayClientMessage(new TranslatableComponent("pixelLoader.LoadingThread.fill"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level worldIn, @Nonnull Player playerIn, @Nonnull InteractionHand handIn) {
        if (playerIn.isShiftKeyDown()) {
            if (!worldIn.isClientSide) Setting.addindex(playerIn);
        } else {
            if (worldIn.isClientSide)
                Minecraft.getInstance().setScreen(new MapSettingScreen());
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }



    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag context) {
        tooltip.add(new TranslatableComponent("pixelLoader.imgLoader.tip"));
        super.appendHoverText(stack, world, tooltip, context);
    }
}
