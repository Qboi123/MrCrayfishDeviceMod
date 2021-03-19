package com.mrcrayfish.device;

import com.mrcrayfish.device.init.DeviceBlocks;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DeviceGroup extends ItemGroup {
	public DeviceGroup(String label) {
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(DeviceBlocks.LAPTOP, 1, DyeColor.RED.getMetadata());
	}
}
