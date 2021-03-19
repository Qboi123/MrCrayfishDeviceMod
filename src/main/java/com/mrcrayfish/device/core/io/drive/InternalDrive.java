package com.mrcrayfish.device.core.io.drive;

import com.mrcrayfish.device.core.io.ServerFolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * @author MrCrayfish
 */
public final class InternalDrive extends AbstractDrive
{
    public InternalDrive(String name)
    {
        super(name);
    }

    @Nullable
    public static AbstractDrive fromTag(CompoundNBT driveTag)
    {
        AbstractDrive drive = new InternalDrive(driveTag.getString("name"));
        if(driveTag.hasKey("root", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT folderTag = driveTag.getCompoundTag("root");
            drive.root = ServerFolder.fromTag(folderTag.getString("file_name"), folderTag.getCompoundTag("data"));
        }
        return drive;
    }

    @Override
    public CompoundNBT toTag()
    {
        CompoundNBT driveTag = new CompoundNBT();
        driveTag.setString("name", name);

        CompoundNBT folderTag = new CompoundNBT();
        folderTag.setString("file_name", root.getName());
        folderTag.setTag("data", root.toTag());
        driveTag.setTag("root", folderTag);

        return driveTag;
    }

    @Override
    public Type getType()
    {
        return Type.INTERNAL;
    }
}
