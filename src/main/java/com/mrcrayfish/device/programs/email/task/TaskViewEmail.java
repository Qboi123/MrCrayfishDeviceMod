package com.mrcrayfish.device.programs.email.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.programs.email.object.Email;
import com.mrcrayfish.device.programs.email.EmailManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.List;

public class TaskViewEmail extends Task
{
	private int index;
	
	public TaskViewEmail() 
	{
		super("view_email");
	}
	
	public TaskViewEmail(int index) 
	{
		this();
		this.index = index;
	}
	
	@Override
	public void prepareRequest(CompoundNBT nbt) 
	{
		nbt.setInteger("Index", this.index);
	}

	@Override
	public void processRequest(CompoundNBT nbt, World world, EntityPlayer player) 
	{
		List<Email> emails = EmailManager.INSTANCE.getEmailsForAccount(player);
		if(emails != null)
		{
			int index = nbt.getInteger("Index");
			if(index >= 0 && index < emails.size())
			{
				emails.get(index).setRead(true);
			}
		}
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) {}

	@Override
	public void processResponse(CompoundNBT nbt) {}
	
}
