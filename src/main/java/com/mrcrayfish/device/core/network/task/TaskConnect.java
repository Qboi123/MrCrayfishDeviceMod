package com.mrcrayfish.device.core.network.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.tileentity.NetworkDeviceTileEntity;
import com.mrcrayfish.device.tileentity.RouterTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author MrCrayfish
 */
public class TaskConnect extends Task
{
    private BlockPos devicePos;
    private BlockPos routerPos;

    public TaskConnect()
    {
        super("connect");
    }

    public TaskConnect(BlockPos devicePos, BlockPos routerPos)
    {
        this();
        this.devicePos = devicePos;
        this.routerPos = routerPos;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {
        nbt.setLong("devicePos", devicePos.toLong());
        nbt.setLong("routerPos", routerPos.toLong());
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("routerPos")));
        if(tileEntity instanceof RouterTileEntity)
        {
            RouterTileEntity tileEntityRouter = (RouterTileEntity) tileEntity;
            Router router = tileEntityRouter.getRouter();

            TileEntity tileEntity1 = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
            if(tileEntity1 instanceof NetworkDeviceTileEntity)
            {
                NetworkDeviceTileEntity tileEntityNetworkDevice = (NetworkDeviceTileEntity) tileEntity1;
                if(router.addDevice(tileEntityNetworkDevice))
                {
                    tileEntityNetworkDevice.connect(router);
                    this.setSuccessful();
                }
            }
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt)
    {

    }

    @Override
    public void processResponse(CompoundNBT nbt)
    {

    }
}
