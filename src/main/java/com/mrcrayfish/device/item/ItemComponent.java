package com.mrcrayfish.device.item;

import com.mrcrayfish.device.MrCrayfishDeviceMod;
import net.minecraft.item.Item;

/**
 * @author MrCrayfish
 */
public class ItemComponent extends Item
{
    public ItemComponent(String id)
    {
        this.setUnlocalizedName(id);
        this.setRegistryName(id);
        this.setCreativeTab(MrCrayfishDeviceMod.ITEM_GROUP);
    }
}
