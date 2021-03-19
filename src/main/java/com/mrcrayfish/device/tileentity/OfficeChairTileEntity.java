package com.mrcrayfish.device.tileentity;

import com.mrcrayfish.device.entity.SeatEntity;
import com.mrcrayfish.device.util.IColored;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OfficeChairTileEntity extends SyncableTileEntity implements IColored {
    private DyeColor color = DyeColor.RED;
    private float rotation;

    public OfficeChairTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public DyeColor getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColor color) {
        this.color = color;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if(compound.contains("color", Constants.NBT.TAG_BYTE)) {
            color = DyeColor.byId(compound.getByte("color"));
            rotation = compound.getFloat("color");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putByte("color", (byte) color.getId());
        compound.putFloat("color", rotation);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        tag.putByte("color", (byte) color.getId());
        return tag;
    }

    @OnlyIn(Dist.CLIENT)
    public float getRotation() {
        List<SeatEntity> seats;
        if (world != null) {
            seats = world.getEntitiesWithinAABB(SeatEntity.class, new AxisAlignedBB(pos));
            if(!seats.isEmpty()) {
                SeatEntity seat = seats.get(0);
                if(seat.getControllingPassenger() != null) {
                    if(seat.getControllingPassenger() instanceof LivingEntity) {
                        LivingEntity living = (LivingEntity) seat.getControllingPassenger();
                        living.renderYawOffset = living.rotationYaw;
                        living.prevRenderYawOffset = living.rotationYaw;
                        return living.rotationYaw;
                    }

                    return seat.getControllingPassenger().rotationYaw;
                }
            }
        }

        return rotation;
    }
}
