package com.mrcrayfish.device.programs.email.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.programs.email.EmailManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskRegisterEmailAccount extends Task
{
	private String name;
	
	public TaskRegisterEmailAccount() 
	{
		super("register_email_account");
	}
	
	public TaskRegisterEmailAccount(String name) 
	{
		this();
		this.name = name;
	}

	@Override
	public void prepareRequest(CompoundNBT nbt) 
	{
		nbt.setString("AccountName", this.name);
	}

	@Override
	public void processRequest(CompoundNBT nbt, World world, EntityPlayer player) 
	{
		if(EmailManager.INSTANCE.addAccount(player, nbt.getString("AccountName")))
		{
			this.setSuccessful();
		}	
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) {}

	@Override
	public void processResponse(CompoundNBT nbt) {}

}
