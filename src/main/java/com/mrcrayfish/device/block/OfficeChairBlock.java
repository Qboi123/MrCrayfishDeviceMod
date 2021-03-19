package com.mrcrayfish.device.block;

import com.mrcrayfish.device.MrCrayfishDeviceMod;
import com.mrcrayfish.device.entity.SeatEntity;
import com.mrcrayfish.device.object.Bounds;
import com.mrcrayfish.device.tileentity.OfficeChairTileEntity;
import com.mrcrayfish.device.util.SeatUtil;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.Property;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author MrCrayfish
 */
public class OfficeChairBlock extends DeviceBlock.Colored
{
    public static final Property<Type> TYPE = Property.create("type", Type.class);

    private static final AxisAlignedBB EMPTY_BOX = new Bounds(0, 0, 0, 0, 0, 0).toAABB();
    private static final AxisAlignedBB SELECTION_BOX = new Bounds(1, 0, 1, 15, 27, 15).toAABB();
    private static final AxisAlignedBB SEAT_BOUNDING_BOX = new Bounds(1, 0, 1, 15, 10, 15).toAABB();

    public OfficeChairBlock()
    {
        super(Material.ROCK);
        this.setUnlocalizedName("office_chair");
        this.setRegistryName("office_chair");
        this.setCreativeTab(MrCrayfishDeviceMod.ITEM_GROUP);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH).withProperty(BlockColored.COLOR, DyeColor.RED).withProperty(TYPE, Type.LEGS));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, Direction facing)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if(Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getRidingEntity() instanceof SeatEntity)
        {
            return EMPTY_BOX;
        }
        return SELECTION_BOX;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return SEAT_BOUNDING_BOX;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote)
        {
            SeatUtil.createSeatAndSit(worldIn, pos, playerIn, 0.5);
        }
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new OfficeChairTileEntity();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, BlockColored.COLOR, TYPE);
    }

    public enum Type implements IStringSerializable
    {
        LEGS, SEAT, FULL;

        @Override
        public String getName()
        {
            return name().toLowerCase();
        }
    }
}
