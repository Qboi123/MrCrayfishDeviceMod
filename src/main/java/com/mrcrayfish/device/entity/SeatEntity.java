package com.mrcrayfish.device.entity;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SeatEntity extends Entity {
    public SeatEntity(EntityType<SeatEntity> entityType, World world) {
        super(entityType, world);
//        this.setSize(0.001F, 0.001F);
        this.setInvisible(true);
    }

    public SeatEntity(EntityType<SeatEntity> type, World worldIn, BlockPos pos, double yOffset) {
        this(type, worldIn);
        this.setPosition(pos.getX() + 0.5, pos.getY() + yOffset, pos.getZ() + 0.5);
    }

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    @Override
    public void tick() {
        if(!this.world.isRemote && (!this.isBeingRidden() || this.world.isAirBlock(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ())))) {
            this.setDead();
        }
    }

    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void preparePlayerToSpawn() {
        super.preparePlayerToSpawn();
    }

    @Override
    public void read(CompoundNBT compound) {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }
}
