package com.mrcrayfish.device.core.io.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.core.io.FileSystem;
import com.mrcrayfish.device.core.io.drive.AbstractDrive;
import com.mrcrayfish.device.tileentity.LaptopTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

/**
 * @author MrCrayfish
 */
public class TaskSetupFileBrowser extends Task
{
    private BlockPos pos;
    private boolean includeMain;

    private AbstractDrive mainDrive;
    private Map<UUID, AbstractDrive> availableDrives;

    private TaskSetupFileBrowser()
    {
        super("get_file_system");
    }

    public TaskSetupFileBrowser(BlockPos pos, boolean includeMain)
    {
        this();
        this.pos = pos;
        this.includeMain = includeMain;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {
        nbt.setLong("pos", pos.toLong());
        nbt.setBoolean("include_main", includeMain);
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("pos")));
        if(tileEntity instanceof LaptopTileEntity)
        {
            LaptopTileEntity laptop = (LaptopTileEntity) tileEntity;
            FileSystem fileSystem = laptop.getFileSystem();
            if(nbt.getBoolean("include_main"))
            {
                mainDrive = fileSystem.getMainDrive();
            }
            availableDrives = fileSystem.getAvailableDrives(world, false);
            this.setSuccessful();
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt)
    {
        if(this.isSucessful())
        {
            if(mainDrive != null)
            {
                CompoundNBT mainDriveTag = new CompoundNBT();
                mainDriveTag.setString("name", mainDrive.getName());
                mainDriveTag.setString("uuid", mainDrive.getUUID().toString());
                mainDriveTag.setString("type", mainDrive.getType().toString());
                nbt.setTag("main_drive", mainDriveTag);
                nbt.setTag("structure", mainDrive.getDriveStructure().toTag());
            }

            NBTTagList driveList = new NBTTagList();
            availableDrives.forEach((k, v) -> {
                CompoundNBT driveTag = new CompoundNBT();
                driveTag.setString("name", v.getName());
                driveTag.setString("uuid", v.getUUID().toString());
                driveTag.setString("type", v.getType().toString());
                driveList.appendTag(driveTag);
            });
            nbt.setTag("available_drives", driveList);
        }
    }

    @Override
    public void processResponse(CompoundNBT nbt)
    {

    }
}
