package com.ywsuoyi.pixelloader;

import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class banBlockInv implements Container {

    @Override
    public int getContainerSize() {
        return 54;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : Setting.banItem) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return index >= 0 && index < Setting.banItem.size() ? Setting.banItem.get(index) : ItemStack.EMPTY;
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@NotNull Player p_18946_) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(Setting.banItem, index, count);
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = Setting.banItem.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            Setting.banItem.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        Setting.banItem.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }



    @Override
    public void clearContent() {
        Setting.banItem.clear();
    }
}
