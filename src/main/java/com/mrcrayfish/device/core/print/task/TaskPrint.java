package com.mrcrayfish.device.core.print.task;

import com.mrcrayfish.device.api.print.IPrint;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.tileentity.NetworkDeviceTileEntity;
import com.mrcrayfish.device.tileentity.PrinterTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * @author MrCrayfish
 */
public class TaskPrint extends Task
{
    private BlockPos devicePos;
    private UUID printerId;
    private IPrint print;

    private TaskPrint()
    {
        super("print");
    }

    public TaskPrint(BlockPos devicePos, NetworkDevice printer, IPrint print)
    {
        this();
        this.devicePos = devicePos;
        this.printerId = printer.getId();
        this.print = print;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {
        nbt.setLong("devicePos", devicePos.toLong());
        nbt.setUniqueId("printerId", printerId);
        nbt.setTag("print", IPrint.writeToTag(print));
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("devicePos")));
        if(tileEntity instanceof NetworkDeviceTileEntity)
        {
            NetworkDeviceTileEntity device = (NetworkDeviceTileEntity) tileEntity;
            Router router = device.getRouter();
            if(router != null)
            {
                NetworkDeviceTileEntity printer = router.getDevice(world, nbt.getUniqueId("printerId"));
                if(printer != null && printer instanceof PrinterTileEntity)
                {
                    IPrint print = IPrint.loadFromTag(nbt.getCompoundTag("print"));
                    ((PrinterTileEntity) printer).addToQueue(print);
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
