package com.mrcrayfish.device.core.network;

import com.mrcrayfish.device.tileentity.NetworkDeviceTileEntity;
import com.mrcrayfish.device.tileentity.RouterTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author MrCrayfish
 */
public class Connection
{
    private UUID routerId;
    private BlockPos routerPos;

    private Connection() {}

    public Connection(Router router)
    {
        this.routerId = router.getId();
        this.routerPos = router.getPos();
    }

    public UUID getRouterId()
    {
        return routerId;
    }

    @Nullable
    public BlockPos getRouterPos()
    {
        return routerPos;
    }

    public void setRouterPos(BlockPos routerPos)
    {
        this.routerPos = routerPos;
    }

    @Nullable
    public Router getRouter(World world)
    {
        if(routerPos == null)
            return null;

        TileEntity tileEntity = world.getTileEntity(routerPos);
        if(tileEntity instanceof RouterTileEntity)
        {
            RouterTileEntity router = (RouterTileEntity) tileEntity;
            if(router.getRouter().getId().equals(routerId))
            {
                return router.getRouter();
            }
        }
        return null;
    }

    public boolean isConnected()
    {
        return routerPos != null;
    }

    public CompoundNBT toTag()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.setString("id", routerId.toString());
        return tag;
    }

    public static Connection fromTag(NetworkDeviceTileEntity device, CompoundNBT tag)
    {
        Connection connection = new Connection();
        connection.routerId = UUID.fromString(tag.getString("id"));
        return connection;
    }
}
