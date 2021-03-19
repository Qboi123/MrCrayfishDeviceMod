package com.mrcrayfish.device.tileentity;

import com.mrcrayfish.device.api.print.IPrint;
import com.mrcrayfish.device.init.DeviceTileEntites;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PaperTileEntity extends SyncableTileEntity {
    private IPrint print;
    private byte rotation;

    public PaperTileEntity() {
        super(DeviceTileEntites.PAPER.get());
    }

    public void nextRotation()
    {
        rotation++;
        if(rotation > 7)
        {
            rotation = 0;
        }
        pipeline.setByte("rotation", rotation);
        sync();
        playSound(SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM);
    }

    public float getRotation()
    {
        return rotation * 45F;
    }

    @Nullable
    public IPrint getPrint()
    {
        return print;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if(compound.contains("print", Constants.NBT.TAG_COMPOUND)) {
            print = IPrint.loadFromTag(compound.getCompoundTag("print"));
        }
        if(compound.contains("rotation", Constants.NBT.TAG_BYTE)) {
            rotation = compound.getByte("rotation");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if(print != null) {
            compound.put("print", IPrint.writeToTag(print));
        }
        compound.putByte("rotation", rotation);
        return compound;
    }

    @Override
    public CompoundNBT writeSyncTag() {
        CompoundNBT tag = new CompoundNBT();
        if(print != null) {
            tag.put("print", IPrint.writeToTag(print));
        }
        tag.putByte("rotation", rotation);
        return tag;
    }

    private void playSound(SoundEvent sound) {
        if (world != null) {
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
