package com.mrcrayfish.device.block;

import com.mrcrayfish.device.tileentity.DeviceTileEntity;
import com.mrcrayfish.device.util.IColored;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MrCrayfish
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DeviceBlock extends HorizontalBlock
{
    protected DeviceBlock(Properties properties)
    {
        super(properties.hardnessAndResistance(0.5f));
    }

    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.fullCube();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalDirection());
    }
    
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return null;
//    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return new ArrayList<>();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if(tileEntity instanceof DeviceTileEntity) {
            DeviceTileEntity deviceTileEntity = (DeviceTileEntity) tileEntity;
            if(stack.hasDisplayName()) {
                deviceTileEntity.setCustomName(stack.getDisplayName().getString());
            }
        }
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if(!world.isRemote && !player.capabilities.isCreativeMode)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof DeviceTileEntity)
            {
                DeviceTileEntity device = (DeviceTileEntity) tileEntity;

                CompoundNBT tileEntityTag = new CompoundNBT();
                tileEntity.writeToNBT(tileEntityTag);
                tileEntityTag.removeTag("x");
                tileEntityTag.removeTag("y");
                tileEntityTag.removeTag("z");
                tileEntityTag.removeTag("id");
                tileEntityTag.removeTag("color");

                removeTagsForDrop(tileEntityTag);

                CompoundNBT compound = new CompoundNBT();
                compound.setTag("BlockEntityTag", tileEntityTag);

                ItemStack drop;
                if(tileEntity instanceof IColored)
                {
                    drop = new ItemStack(Item.getItemFromBlock(this), 1, ((IColored)tileEntity).getColor().getMetadata());
                }
                else
                {
                    drop = new ItemStack(Item.getItemFromBlock(this));
                }
                drop.setTagCompound(compound);

                if(device.hasCustomName())
                {
                    drop.setStackDisplayName(device.getCustomName());
                }

                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));

                return world.setBlockToAir(pos);
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if(!world.isRemote && !player.capabilities.isCreativeMode)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof DeviceTileEntity)
            {
                DeviceTileEntity device = (DeviceTileEntity) tileEntity;

                CompoundNBT tileEntityTag = new CompoundNBT();
                tileEntity.writeToNBT(tileEntityTag);
                tileEntityTag.removeTag("x");
                tileEntityTag.removeTag("y");
                tileEntityTag.removeTag("z");
                tileEntityTag.removeTag("id");
                tileEntityTag.removeTag("color");

                removeTagsForDrop(tileEntityTag);

                CompoundNBT compound = new CompoundNBT();
                compound.setTag("BlockEntityTag", tileEntityTag);

                ItemStack drop;
                if(tileEntity instanceof IColored)
                {
                    drop = new ItemStack(Item.getItemFromBlock(this), 1, ((IColored)tileEntity).getColor().getMetadata());
                }
                else
                {
                    drop = new ItemStack(Item.getItemFromBlock(this));
                }
                drop.setTagCompound(compound);

                if(device.hasCustomName())
                {
                    drop.setStackDisplayName(device.getCustomName());
                }

                world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));

                return world.setBlockToAir(pos);
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    protected void removeTagsForDrop(CompoundNBT tileEntityTag) {}

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(World world, BlockState state);

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    /**
     * Colored implementation of BlockDevice.
     */
    public static abstract class Colored extends DeviceBlock {
        protected Colored(Material materialIn)
        {
            super(materialIn);
        }

        @Override
        public void getDrops(NonNullList<ItemStack> drops, IBlockReader world, BlockPos pos, BlockState state, int fortune) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof IColored) {
                drops.add(new ItemStack(Item.getItemFromBlock(this), 1, ((IColored) tileEntity).getColor().getMetadata()));
            }
        }

        @Override
        public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof IColored)
            {
                return new ItemStack(Item.getItemFromBlock(this), 1, ((IColored) tileEntity).getColor().getMetadata());
            }
            return super.getPickBlock(state, target, world, pos, player);
        }

        @Override
        public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack stack)
        {
            super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof IColored)
            {
                IColored colored = (IColored) tileEntity;
                colored.setColor(DyeColor.byMetadata(stack.getMetadata()));
            }
        }

        @Override
        public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos)
        {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof IColored)
            {
                IColored colored = (IColored) tileEntity;
                state = state.withProperty(BlockColored.COLOR, colored.getColor());
            }
            return state;
        }

        @Override
        public boolean removedByPlayer(BlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
        {
            if(!world.isRemote && !player.capabilities.isCreativeMode)
            {
                TileEntity tileEntity = world.getTileEntity(pos);
                if(tileEntity instanceof IColored)
                {
                    IColored colored = (IColored) tileEntity;

                    CompoundNBT tileEntityTag = new CompoundNBT();
                    tileEntity.writeToNBT(tileEntityTag);
                    tileEntityTag.removeTag("x");
                    tileEntityTag.removeTag("y");
                    tileEntityTag.removeTag("z");
                    tileEntityTag.removeTag("id");
                    tileEntityTag.removeTag("color");

                    removeTagsForDrop(tileEntityTag);

                    CompoundNBT compound = new CompoundNBT();
                    compound.setTag("BlockEntityTag", tileEntityTag);

                    ItemStack  drop = new ItemStack(Item.getItemFromBlock(this), 1, colored.getColor().getMetadata());
                    drop.setTagCompound(compound);

                    if(tileEntity instanceof DeviceTileEntity)
                    {
                        DeviceTileEntity device = (DeviceTileEntity) tileEntity;
                        if(device.hasCustomName())
                        {
                            drop.setStackDisplayName(device.getCustomName());
                        }
                    }

                    world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));

                    return world.setBlockToAir(pos);
                }
            }
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }

        @Override
        public int getMetaFromState(BlockState state)
        {
            return state.getValue(FACING).getHorizontalIndex();
        }

        @Override
        public BlockState getStateFromMeta(int meta)
        {
            return this.getDefaultState().withProperty(FACING, Direction.getHorizontal(meta));
        }

        @Override
        protected BlockStateContainer createBlockState()
        {
            return new BlockStateContainer(this, FACING, BlockColored.COLOR);
        }
    }
}
