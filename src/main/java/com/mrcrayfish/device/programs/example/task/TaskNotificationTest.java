package com.mrcrayfish.device.programs.example.task;

import com.mrcrayfish.device.api.app.Icons;
import com.mrcrayfish.device.api.app.Notification;
import com.mrcrayfish.device.api.task.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

/**
 * @author MrCrayfish
 */
public class TaskNotificationTest extends Task
{
    public TaskNotificationTest()
    {
        super("notification_test");
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {

    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        Notification notification = new Notification(Icons.MAIL, "New Email!", "Check your inbox");
        notification.pushTo((EntityPlayerMP) player);

       /* MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
        players.forEach(notification::pushTo);*/
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
