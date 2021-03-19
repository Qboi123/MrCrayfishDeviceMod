package com.mrcrayfish.device.util;

import com.mrcrayfish.device.entity.SeatEntity;
import com.mrcrayfish.device.init.DeviceEntityTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author MrCrayfish
 */
public class SeatUtil
{
    public static void createSeatAndSit(World worldIn, BlockPos pos, PlayerEntity playerIn, double yOffset)
    {
        List<SeatEntity> seats = worldIn.getEntitiesWithinAABB(SeatEntity.class, new AxisAlignedBB(pos));
        if(!seats.isEmpty())
        {
            SeatEntity seat = seats.get(0);
            if(seat.getRidingEntity() == null)
            {
                playerIn.startRiding(seat);
            }
        }
        else
        {
            SeatEntity seat = new SeatEntity(DeviceEntityTypes.SEAT.get(), worldIn, pos, yOffset);
            worldIn.addEntity(seat);
            playerIn.startRiding(seat);
        }
    }
}
