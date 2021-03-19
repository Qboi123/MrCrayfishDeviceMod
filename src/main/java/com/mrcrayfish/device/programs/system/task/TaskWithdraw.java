package com.mrcrayfish.device.programs.system.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.programs.system.object.Account;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

/**
 * @author MrCrayfish
 */
public class TaskWithdraw extends Task
{
    private int amount;

    private TaskWithdraw()
    {
        super("bank_withdraw");
    }

    public TaskWithdraw(int amount)
    {
        this();
        this.amount = amount;
    }

    @Override
    public void prepareRequest(CompoundNBT nbt)
    {
        nbt.setInteger("amount", this.amount);
    }

    @Override
    public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
    {
        int amount = nbt.getInteger("amount");
        Account account = BankUtil.INSTANCE.getAccount(player);
        if(account.withdraw(amount))
        {
            int stacks = amount / 64;
            for(int i = 0; i < stacks; i++)
            {
                world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.EMERALD, 64)));
            }

            int remaining = amount % 64;
            if(remaining > 0)
            {
                world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.EMERALD, remaining)));
            }

            this.amount = account.getBalance();
            this.setSuccessful();
        }
    }

    @Override
    public void prepareResponse(CompoundNBT nbt)
    {
        nbt.setInteger("balance", this.amount);
    }

    @Override
    public void processResponse(CompoundNBT nbt) {}
}
