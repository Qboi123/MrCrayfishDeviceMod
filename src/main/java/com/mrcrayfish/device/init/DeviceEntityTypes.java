package com.mrcrayfish.device.init;

import com.mrcrayfish.device.Constants;
import com.mrcrayfish.device.entity.SeatEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DeviceEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MOD_ID);
    public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITY_TYPES.register("seat", () -> EntityType.Builder.<SeatEntity>create(SeatEntity::new, EntityClassification.MISC).size(0.0001f, 0.0001f).build("seat"));
}
