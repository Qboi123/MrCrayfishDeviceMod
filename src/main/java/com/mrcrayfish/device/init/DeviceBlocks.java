package com.mrcrayfish.device.init;

import com.mrcrayfish.device.Constants;
import com.mrcrayfish.device.MrCrayfishDeviceMod;
import com.mrcrayfish.device.block.*;
import com.mrcrayfish.device.item.ItemColoredDevice;
import com.mrcrayfish.device.item.ItemPaper;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class DeviceBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

	public static final RegistryObject<LaptopBlock> LAPTOP = register("laptop", LaptopBlock::new);
	public static final RegistryObject<RouterBlock> ROUTER = register("router", RouterBlock::new);
	public static final RegistryObject<PrinterBlock> PRINTER = register("printer", PrinterBlock::new);
	public static final RegistryObject<PaperBlock> PAPER = register("printer", PaperBlock::new);
	public static final RegistryObject<OfficeChairBlock> OFFICE_CHAIR = register("office_chair", OfficeChairBlock::new);

	public static final RegistryObject<BlockItem> LAPTOP_ITEM = registerItem("laptop", LAPTOP);
	public static final RegistryObject<BlockItem> ROUTER_ITEM = registerItem("router", ROUTER);
	public static final RegistryObject<BlockItem> PRINTER_ITEM = registerItem("printer", PRINTER);
	public static final RegistryObject<BlockItem> PAPER_ITEM = registerItem("printer", PAPER);
	public static final RegistryObject<BlockItem> OFFICE_CHAIR_ITEM = registerItem("office_chair", OFFICE_CHAIR);

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier) {
		return BLOCKS.register(name, supplier);
	}

	private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier) {
		return ITEMS.register(name, supplier);
	}

	private static RegistryObject<BlockItem> registerItem(String name, Supplier<? extends Block> supplier) {
		return ITEMS.register(name, () -> new BlockItem(supplier.get(), new Item.Properties().group(MrCrayfishDeviceMod.ITEM_GROUP)));
	}

	static
	{
		LAPTOP = new LaptopBlock();
        ROUTER = new RouterBlock();
		PRINTER = new PrinterBlock();
		PAPER = new PaperBlock();

		OFFICE_CHAIR = new OfficeChairBlock();
	}

	public static void register()
	{
		registerBlock(LAPTOP, new ItemColoredDevice(LAPTOP));
        registerBlock(ROUTER, new ItemColoredDevice(ROUTER));
		registerBlock(PRINTER, new ItemColoredDevice(PRINTER));
		registerBlock(PAPER, new ItemPaper(PAPER));

		registerBlock(OFFICE_CHAIR, new ItemColoredDevice(OFFICE_CHAIR));
	}

	private static void registerBlock(Block block)
	{
		registerBlock(block, new ItemBlock(block));
	}

	private static void registerBlock(Block block, ItemBlock item)
	{
		if(block.getRegistryName() == null)
			throw new IllegalArgumentException("A block being registered does not have a registry name and could be successfully registered.");

		RegistrationHandler.Blocks.add(block);
		item.setRegistryName(block.getRegistryName());
		RegistrationHandler.Items.add(item);
	}
}