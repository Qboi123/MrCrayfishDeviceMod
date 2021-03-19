package com.mrcrayfish.device.tileentity;

import com.mrcrayfish.device.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * @author MrCrayfish
 */
public abstract class SyncableTileEntity extends TileEntity {
    protected CompoundNBT pipeline = new CompoundNBT();

    public SyncableTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void sync() {
        if (world != null) {
            TileEntityUtil.markBlockForUpdate(world, pos);
        }
        markDirty();
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world != null) {
            BlockState state = world.getBlockState(pkt.getPos());
            this.read(state, pkt.getNbtCompound());
        }
    }

    @Override
    public final CompoundNBT getUpdateTag() {
        if(!pipeline.isEmpty()) {
            CompoundNBT updateTag = super.write(pipeline);
            pipeline = new CompoundNBT();
            return updateTag;
        }

        return super.write(writeSyncTag());
    }

    public abstract CompoundNBT writeSyncTag();

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    public CompoundNBT getPipeline() {
        return pipeline;
    }
}
