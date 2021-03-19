package com.mrcrayfish.device.programs.system.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.tileentity.LaptopTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaskUpdateSystemData extends Task
{
    private BlockPos pos;
    private CompoundNBT data;

    public TaskUpdateSystemData()
    {
        super("update_system_data");
    }

    public TaskUpdateSystemData(BlockPos pos, CompoundNBT data)
    {
        this();
        this.pos = pos;
        this.data = data;
    }

    @Override
    public void prepareRequest(CompoundNBT tag)
    {
        tag.setLong("pos", pos.toLong());
        tag.setTag("data", this.data);
    }

    @Override
    public void processRequest(CompoundNBT tag, World world, EntityPlayer player)
    {
        BlockPos pos = BlockPos.fromLong(tag.getLong("pos"));
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof LaptopTileEntity)
        {
            LaptopTileEntity laptop = (LaptopTileEntity) tileEntity;
            laptop.setSystemData(tag.getCompoundTag("data"));
        }
        this.setSuccessful();
    }

    @Override
    public void prepareResponse(CompoundNBT tag)
    {

    }

    @Override
    public void processResponse(CompoundNBT tag)
    {

    }
}
