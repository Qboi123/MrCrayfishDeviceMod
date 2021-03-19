package com.mrcrayfish.device.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

/**
 * @author MrCrayfish
 */
public class ItemPaper extends ItemBlock
{
    public ItemPaper(Block block)
    {
        super(block);
        this.setMaxStackSize(1);
    }



    @Nullable
    @Override
    public CompoundNBT getNBTShareTag(ItemStack stack)
    {
        CompoundNBT tag = stack.getTagCompound();
        if(tag != null)
        {
            CompoundNBT copy = tag.copy();
            copy.removeTag("BlockEntityTag");
            return copy;
        }
        return null;
    }
}
