package com.mrcrayfish.device;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.print.PrintingManager;
import com.mrcrayfish.device.api.task.TaskManager;
import com.mrcrayfish.device.core.io.task.*;
import com.mrcrayfish.device.core.network.task.TaskConnect;
import com.mrcrayfish.device.core.network.task.TaskGetDevices;
import com.mrcrayfish.device.core.network.task.TaskPing;
import com.mrcrayfish.device.core.print.task.TaskPrint;
import com.mrcrayfish.device.core.task.TaskInstallApp;
import com.mrcrayfish.device.event.BankEvents;
import com.mrcrayfish.device.event.EmailEvents;
import com.mrcrayfish.device.gui.GuiHandler;
import com.mrcrayfish.device.init.DeviceTileEntites;
import com.mrcrayfish.device.init.RegistrationHandler;
import com.mrcrayfish.device.network.PacketHandler;
import com.mrcrayfish.device.programs.*;
import com.mrcrayfish.device.programs.debug.ApplicationTextArea;
import com.mrcrayfish.device.programs.email.ApplicationEmail;
import com.mrcrayfish.device.programs.email.task.*;
import com.mrcrayfish.device.programs.example.ApplicationExample;
import com.mrcrayfish.device.programs.example.task.TaskNotificationTest;
import com.mrcrayfish.device.programs.gitweb.ApplicationGitWeb;
import com.mrcrayfish.device.programs.system.ApplicationAppStore;
import com.mrcrayfish.device.programs.system.ApplicationBank;
import com.mrcrayfish.device.programs.system.ApplicationFileBrowser;
import com.mrcrayfish.device.programs.system.ApplicationSettings;
import com.mrcrayfish.device.programs.system.task.*;
import com.mrcrayfish.device.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MOD_ID)
public class MrCrayfishDeviceMod {
	public static MrCrayfishDeviceMod instance;

	public static CommonProxy proxy;
	
	public static final DeviceGroup ITEM_GROUP = new DeviceGroup("cdmTabDevice");

	private static Logger LOGGER = LogManager.getLogger("DeviceMod");

	public static final boolean DEVELOPER_MODE = true;

	public MrCrayfishDeviceMod() {
		// Tile Entity Registering
		DeviceTileEntites.register();

		// Packet Registering
		PacketHandler.init();
	}

	public void isDeveloperMode() {
		DEVELOPER_MODE;
	}

	public void isModDevEnvironment() {
		if (isClientSide()) {
			return Minecraft.getInstance().
		}
	}

	public static boolean isClientSide() {
		try {
			Class.forName("net.minecraft.client.Minecraft");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private boolean isModDevEnvironment0() {
		try {
			Class.forName("net.minecraftforge.userdev.LaunchTesting");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public void commonSetup(FMLCommonSetupEvent event) throws LaunchException {
		if(DEVELOPER_MODE && !(Boolean) event.get) {
			throw new LaunchException();
		}

		DeviceConfig.load(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new DeviceConfig());

		RegistrationHandler.init();
		
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLCommonSetupEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		MinecraftForge.EVENT_BUS.register(new EmailEvents());
		MinecraftForge.EVENT_BUS.register(new BankEvents());

		registerApplications();

		proxy.clientSetup();
	}
	
	@EventHandler
	public void postInit(FMLLoadCompleteEvent event)
	{
		proxy.postInit();
	}

	private void registerApplications() {
		// Applications (Both)
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "settings"), ApplicationSettings.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "bank"), ApplicationBank.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "file_browser"), ApplicationFileBrowser.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "gitweb"), ApplicationGitWeb.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "note_stash"), ApplicationNoteStash.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "pixel_painter"), ApplicationPixelPainter.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "ender_mail"), ApplicationEmail.class);
		ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "app_store"), ApplicationAppStore.class);

		// Core
		TaskManager.registerTask(TaskInstallApp.class);
		TaskManager.registerTask(TaskUpdateApplicationData.class);
		TaskManager.registerTask(TaskPrint.class);
		TaskManager.registerTask(TaskUpdateSystemData.class);
		TaskManager.registerTask(TaskConnect.class);
		TaskManager.registerTask(TaskPing.class);
		TaskManager.registerTask(TaskGetDevices.class);

		//Bank
		TaskManager.registerTask(TaskDeposit.class);
		TaskManager.registerTask(TaskWithdraw.class);
		TaskManager.registerTask(TaskGetBalance.class);
		TaskManager.registerTask(TaskPay.class);
		TaskManager.registerTask(TaskAdd.class);
		TaskManager.registerTask(TaskRemove.class);

		//File browser
		TaskManager.registerTask(TaskSendAction.class);
		TaskManager.registerTask(TaskSetupFileBrowser.class);
		TaskManager.registerTask(TaskGetFiles.class);
		TaskManager.registerTask(TaskGetStructure.class);
		TaskManager.registerTask(TaskGetMainDrive.class);

		//Ender Mail
		TaskManager.registerTask(TaskUpdateInbox.class);
		TaskManager.registerTask(TaskSendEmail.class);
		TaskManager.registerTask(TaskCheckEmailAccount.class);
		TaskManager.registerTask(TaskRegisterEmailAccount.class);
		TaskManager.registerTask(TaskDeleteEmail.class);
		TaskManager.registerTask(TaskViewEmail.class);

		if(!DEVELOPER_MODE)
		{
			// Applications (Normal)
			//ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "boat_racers"), ApplicationBoatRacers.class);
			//ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "mine_bay"), ApplicationMineBay.class);

			// Tasks (Normal)
			//TaskManager.registerTask(TaskAddAuction.class);
			//TaskManager.registerTask(TaskGetAuctions.class);
			//TaskManager.registerTask(TaskBuyItem.class);
		}
		else
		{
			// Applications (Developers)
			ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "example"), ApplicationExample.class);
			ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "icons"), ApplicationIcons.class);
			ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "text_area"), ApplicationTextArea.class);
			ApplicationManager.registerApplication(new ResourceLocation(Constants.MOD_ID, "test"), ApplicationTest.class);

			TaskManager.registerTask(TaskNotificationTest.class);
		}

		PrintingManager.registerPrint(new ResourceLocation(Constants.MOD_ID, "picture"), ApplicationPixelPainter.PicturePrint.class);
	}

	public static Logger getLogger()
	{
		return logger;
	}
}
