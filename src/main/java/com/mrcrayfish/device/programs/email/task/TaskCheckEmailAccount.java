package com.mrcrayfish.device.programs.email.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.programs.email.EmailManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskCheckEmailAccount extends Task 
{
	private boolean hasAccount = false;
	private String name = null;
	
	public TaskCheckEmailAccount()
	{
		super("check_email_account");
	}

	@Override
	public void prepareRequest(CompoundNBT nbt) {}

	@Override
	public void processRequest(CompoundNBT nbt, World world, EntityPlayer player) 
	{
		this.hasAccount = EmailManager.INSTANCE.hasAccount(player.getUniqueID());
		if(this.hasAccount)
		{
			this.name = EmailManager.INSTANCE.getName(player);
			this.setSuccessful();
		}
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) 
	{
		if(this.isSucessful()) nbt.setString("Name", this.name);
	}

	@Override
	public void processResponse(CompoundNBT nbt) {}

}
