package com.mrcrayfish.device.api.print;

import com.mrcrayfish.device.init.DeviceBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

//printing somethings takes makes ink cartridge take damage. cartridge can only stack to one

/**
 * @author MrCrayfish
 */
public interface IPrint
{
    String getName();

    /**
     * Gets the speed of the print. The higher the value, the longer it will take to print.
     * @return the speed of this print
     */
    int speed();

    /**
     * Gets whether or not this print requires colored ink.
     * @return if print requires ink
     */
    boolean requiresColor();

    /**
     * Converts print into an NBT tag compound. Used for the renderer.
     * @return nbt form of print
     */
    CompoundNBT toTag();

    void fromTag(CompoundNBT tag);

    @SideOnly(Side.CLIENT)
    Class<? extends Renderer> getRenderer();

    static CompoundNBT writeToTag(IPrint print)
    {
        CompoundNBT tag = new CompoundNBT();
        tag.setString("type", PrintingManager.getPrintIdentifier(print));
        tag.setTag("data", print.toTag());
        return tag;
    }

    @Nullable
    static IPrint loadFromTag(CompoundNBT tag)
    {
        IPrint print = PrintingManager.getPrint(tag.getString("type"));
        if(print != null)
        {
            print.fromTag(tag.getCompoundTag("data"));
            return print;
        }
        return null;
    }

    static ItemStack generateItem(IPrint print)
    {
        CompoundNBT blockEntityTag = new CompoundNBT();
        blockEntityTag.setTag("print", writeToTag(print));

        CompoundNBT itemTag = new CompoundNBT();
        itemTag.setTag("BlockEntityTag", blockEntityTag);

        ItemStack stack = new ItemStack(DeviceBlocks.PAPER);
        stack.setTagCompound(itemTag);

        if(print.getName() != null && !print.getName().isEmpty())
        {
            stack.setStackDisplayName(print.getName());
        }
        return stack;
    }

    interface Renderer
    {
        boolean render(CompoundNBT data);
    }
}
