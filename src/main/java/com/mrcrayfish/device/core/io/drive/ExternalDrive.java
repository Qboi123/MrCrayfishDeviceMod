package com.mrcrayfish.device.core.io.drive;

import com.mrcrayfish.device.core.io.ServerFolder;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author MrCrayfish
 */
public final class ExternalDrive extends AbstractDrive
{
    private static final Predicate<CompoundNBT> PREDICATE_DRIVE_TAG = tag ->
            tag.hasKey("name", Constants.NBT.TAG_STRING)
            && tag.hasKey("uuid", Constants.NBT.TAG_STRING)
            && tag.hasKey("root", Constants.NBT.TAG_COMPOUND);

    private ExternalDrive() {}

    public ExternalDrive(String displayName)
    {
        super(displayName);
    }

    @Nullable
    public static AbstractDrive fromTag(CompoundNBT driveTag)
    {
        if(!PREDICATE_DRIVE_TAG.test(driveTag))
            return null;

        AbstractDrive drive = new ExternalDrive();
        drive.name = driveTag.getString("name");
        drive.uuid = UUID.fromString(driveTag.getString("uuid"));

        CompoundNBT folderTag = driveTag.getCompoundTag("root");
        drive.root = ServerFolder.fromTag(folderTag.getString("file_name"), folderTag.getCompoundTag("data"));

        return drive;
    }

    @Override
    public CompoundNBT toTag()
    {
        CompoundNBT driveTag = new CompoundNBT();
        driveTag.setString("name", name);
        driveTag.setString("uuid", uuid.toString());

        CompoundNBT folderTag = new CompoundNBT();
        folderTag.setString("file_name", root.getName());
        folderTag.setTag("data", root.toTag());
        driveTag.setTag("root", folderTag);

        return driveTag;
    }

    @Override
    public Type getType()
    {
        return Type.EXTERNAL;
    }
}
