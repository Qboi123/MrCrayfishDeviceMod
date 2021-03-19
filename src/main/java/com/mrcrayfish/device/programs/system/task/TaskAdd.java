package com.mrcrayfish.device.programs.system.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.programs.system.object.Account;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskAdd extends Task 
{
	private int amount;
	
	public TaskAdd()
	{
		super("bank_add");
	}
	
	public TaskAdd(int amount)
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
		Account sender = BankUtil.INSTANCE.getAccount(player);
		sender.add(amount);
		this.setSuccessful();
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) 
	{
		nbt.setInteger("balance", this.amount);
	}

	@Override
	public void processResponse(CompoundNBT nbt) {}
}
