package com.mrcrayfish.device.programs.email.task;

import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.programs.email.object.Email;
import com.mrcrayfish.device.programs.email.EmailManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class TaskSendEmail extends Task 
{
	private Email email;
	private String to;
	
	public TaskSendEmail() 
	{
		super("send_email");
	}
	
	public TaskSendEmail(Email email, String to)
	{
		this();
		this.email = email;
		this.to = to;
	}

	@Override
	public void prepareRequest(CompoundNBT nbt) 
	{
		this.email.writeToNBT(nbt);
		nbt.setString("to", this.to);
	}

	@Override
	public void processRequest(CompoundNBT nbt, World world, EntityPlayer player) 
	{
		String name = EmailManager.INSTANCE.getName(player);
		if(name != null)
		{
			Email email = Email.readFromNBT(nbt);
			email.setAuthor(name);
			if(EmailManager.INSTANCE.addEmailToInbox(email, nbt.getString("to"))) 
			{
				this.setSuccessful();
			}
		}
	}

	@Override
	public void prepareResponse(CompoundNBT nbt) {}

	@Override
	public void processResponse(CompoundNBT nbt) {}
	
}
