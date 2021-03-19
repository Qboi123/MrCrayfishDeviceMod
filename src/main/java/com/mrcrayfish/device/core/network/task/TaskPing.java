package com.mrcrayfish.device.core.network.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.tileentity.NetworkDeviceTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MrCrayfish
 */
public class TaskPing extends Task
{
    private BlockPos sourceDevicePos;
    private int strength;

    private TaskPing()
    {
        super("ping");
    }

    public TaskPing(BlockPos sourceDevicePos)
    {
        this();
        this.sourceDevicePos = sourceDevicePos;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {
        nbt.setLong("sourceDevicePos", sourceDevicePos.toLong());
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("sourceDevicePos")));
        if(tileEntity instanceof NetworkDeviceTileEntity)
        {
            NetworkDeviceTileEntity tileEntityNetworkDevice = (NetworkDeviceTileEntity) tileEntity;
            if(tileEntityNetworkDevice.isConnected())
            {
                this.strength = tileEntityNetworkDevice.getSignalStrength();
                this.setSuccessful();
            }
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt)
    {
        if(this.isSucessful())
        {
            nbt.setInteger("strength", strength);
        }
    }

    @Override
    public void processResponse(CompoundNBT nbt)
    {

    }
}
