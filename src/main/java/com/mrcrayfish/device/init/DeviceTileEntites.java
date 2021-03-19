package com.mrcrayfish.device.init;

import com.mrcrayfish.device.Constants;
import com.mrcrayfish.device.tileentity.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DeviceTileEntites 
{
	private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Constants.MOD_ID);
	public static final RegistryObject<TileEntityType<PaperTileEntity>> PAPER = register("printed_paper", () -> TileEntityType.Builder.create(() -> new PaperTileEntity(), DeviceBlocks.PAPER.get()).build("null"));
	public static final RegistryObject<TileEntityType<PaperTileEntity>> PAPER = register("printed_paper", () -> TileEntityType.Builder.create(() -> new PaperTileEntity(), DeviceBlocks.PAPER.get()).build("null"));

	/**
	 * Register tile entity.
	 *
	 * @param name     the registry name.
	 * @param supplier supplier for registration.
	 * @param <T>      tile-entity to register.
	 * @return an registry object of the tile-entity type.
	 */
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<TileEntityType<T>> supplier) {
		return TILE_ENTITIES.register(name, supplier);
	}

    public static void register() {
		GameRegistry.registerTileEntity(LaptopTileEntity.class, "cdm:laptop");
        GameRegistry.registerTileEntity(RouterTileEntity.class, "cdm:router");
		GameRegistry.registerTileEntity(PrinterTileEntity.class, "cdm:printer");
		GameRegistry.registerTileEntity(PaperTileEntity.class, "cdm:printed_paper");
		GameRegistry.registerTileEntity(OfficeChairTileEntity.class, "cdm:office_chair");
	}
}
