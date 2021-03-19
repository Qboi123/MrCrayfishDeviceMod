package com.mrcrayfish.device.programs.system.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.api.utils.BankUtil;
import com.mrcrayfish.device.programs.system.object.Account;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskGetBalance extends Task 
{
	private int balance;
	
	public TaskGetBalance()
	{
		super("bank_get_balance");
	}

	@Override
	public void prepareRequest(CompoundNBT nbt) {}

	@Override
	public void processRequest(CompoundNBT nbt, World world, EntityPlayer player)
	{
		Account account = BankUtil.INSTANCE.getAccount(player);
		this.balance = account.getBalance();
		this.setSuccessful();
	}

	@Override
	public void prepareResponse(CompoundNBT nbt)
	{
		nbt.setInteger("balance", this.balance);
	}

	@Override
	public void processResponse(CompoundNBT nbt) {}
}
