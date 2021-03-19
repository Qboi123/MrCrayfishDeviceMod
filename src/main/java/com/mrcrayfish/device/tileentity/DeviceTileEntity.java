package com.mrcrayfish.device.tileentity;

import com.mrcrayfish.device.util.IColored;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DeviceTileEntity extends SyncableTileEntity implements ITickableTileEntity
{
    private DyeColor color = DyeColor.RED;
    private UUID deviceId;
    private String name;

    public DeviceTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public final UUID getId() {
        if(deviceId == null) {
            deviceId = UUID.randomUUID();
        }
        return deviceId;
    }

    public abstract String getDeviceName();

    public String getCustomName() {
        return hasCustomName() ? name : getDeviceName();
    }

    public void setCustomName(String name) {
       this.name = name;
    }

    public boolean hasCustomName() {
        return !StringUtils.isEmpty(name);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        compound.putString("deviceId", getId().toString());

        if(hasCustomName()) {
            compound.putString("name", name);
        }

        compound.putByte("color", (byte) color.getId());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if(compound.contains("deviceId", Constants.NBT.TAG_STRING)) {
            deviceId = UUID.fromString(compound.getString("deviceId"));
        }
        if(compound.contains("name", Constants.NBT.TAG_STRING)) {
            name = compound.getString("name");
        }
        if(compound.contains("color", Constants.NBT.TAG_BYTE)) {
            this.color = DyeColor.byId(compound.getByte("color"));
        }
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        if(hasCustomName()) {
            tag.putString("name", name);
        }
        tag.putInt("color", color.getId());
        return tag;
    }

    @Override
    public abstract void tick();

    public static abstract class Colored extends DeviceTileEntity implements IColored {
        private DyeColor color = DyeColor.RED;

        public Colored(TileEntityType<?> tileEntityTypeIn) {
            super(tileEntityTypeIn);
        }

        @Override
        public void read(BlockState state, CompoundNBT compound) {
            super.read(state, compound);
            if(compound.contains("color", Constants.NBT.TAG_BYTE)) {
                this.color = DyeColor.byId(compound.getByte("color"));
            }
        }

        @Override
        public CompoundNBT write(CompoundNBT compound) {
            super.write(compound);
            compound.putByte("color", (byte) color.getId());
            return compound;
        }

        @Override
        public CompoundNBT writeSyncTag() {
            CompoundNBT tag = super.writeSyncTag();
            tag.putByte("color", (byte) color.getId());
            return tag;
        }

        @Override
        public final void setColor(DyeColor color) {
            this.color = color;
        }

        @Override
        public final DyeColor getColor()
        {
            return color;
        }
    }
}
