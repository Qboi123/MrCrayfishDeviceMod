package com.mrcrayfish.device.programs.system.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.programs.system.object.Account;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskRemove extends Task 
{
	private int amount;
	
	public TaskRemove()
	{
		super("bank_remove");
	}
	
	public TaskRemove(int amount)
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
		this.amount = nbt.getInteger("amount");
		Account sender = BankUtil.INSTANCE.getAccount(player);
		if(sender.hasAmount(amount))
		{
			sender.remove(amount);
			this.setSuccessful();
		}
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) 
	{
		if(isSucessful())
		{
			nbt.setInteger("balance", this.amount);
		}
	}

	@Override
	public void processResponse(CompoundNBT nbt) {}
}
