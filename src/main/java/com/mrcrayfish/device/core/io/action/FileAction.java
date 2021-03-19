package com.mrcrayfish.device.core.io.action;

import com.mrcrayfish.device.api.io.File;
import com.mrcrayfish.device.api.io.Folder;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author MrCrayfish
 */
public class FileAction
{
    private Type type;
    private CompoundNBT data;

    private FileAction(Type type, CompoundNBT data)
    {
        this.type = type;
        this.data = data;
    }

    public CompoundNBT toTag()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.setInteger("type", type.ordinal());
        tag.setTag("data", data);
        return tag;
    }

    public static FileAction fromTag(CompoundNBT tag)
    {
        Type type = Type.values()[tag.getInteger("type")];
        CompoundNBT data = tag.getCompoundTag("data");
        return new FileAction(type, data);
    }

    public Type getType()
    {
        return type;
    }

    public CompoundNBT getData()
    {
        return data;
    }

    public enum Type
    {
        NEW, DELETE, RENAME, DATA, COPY_CUT
    }

    public static class Factory
    {
        public static FileAction makeNew(Folder parent, File file, boolean override)
        {
            CompoundNBT vars = new CompoundNBT();
            vars.setString("directory", parent.getPath());
            vars.setString("file_name", file.getName());
            vars.setBoolean("override", override);
            vars.setTag("data", file.toTag());
            return new FileAction(Type.NEW, vars);
        }

        public static FileAction makeDelete(File file)
        {
            CompoundNBT vars = new CompoundNBT();
            vars.setString("directory", file.getLocation());
            vars.setString("file_name", file.getName());
            return new FileAction(Type.DELETE, vars);
        }

        public static FileAction makeRename(File file, String newFileName)
        {
            CompoundNBT vars = new CompoundNBT();
            vars.setString("directory", file.getLocation());
            vars.setString("file_name", file.getName());
            vars.setString("new_file_name", newFileName);
            return new FileAction(Type.RENAME, vars);
        }

        public static FileAction makeData(File file, CompoundNBT data)
        {
            CompoundNBT vars = new CompoundNBT();
            vars.setString("directory", file.getLocation());
            vars.setString("file_name", file.getName());
            vars.setTag("data", data);
            return new FileAction(Type.DATA, vars);
        }

        public static FileAction makeCopyCut(File source, Folder destination, boolean override, boolean cut)
        {
            CompoundNBT vars = new CompoundNBT();
            vars.setString("directory", source.getLocation());
            vars.setString("file_name", source.getName());
            vars.setString("destination_drive", destination.getDrive().getUUID().toString());
            vars.setString("destination_folder", destination.getPath());
            vars.setBoolean("override", override);
            vars.setBoolean("cut", cut);
            return new FileAction(Type.COPY_CUT, vars);
        }
    }
}
