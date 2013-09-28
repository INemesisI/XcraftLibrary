package de.xcraft.INemesisI.Library;

import org.bukkit.plugin.java.JavaPlugin;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftConfigManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Library.Message.Messenger;

//@formatter:off
/***
* @author INemesisI
*     by _____   __                         _      ____
*       /  _/ | / /__  ____ ___  ___  _____(_)____/  _/ 
*       / //  |/ / _ \/ __ `__ \/ _ \/ ___/ / ___// /  
*     _/ // /|  /  __/ / / / / /  __(__  ) (__  )/ /   
*    /___/_/ |_/\___/_/ /_/ /_/\___/____/_/____/___/                                              
*/ 
//@formatter:on

public abstract class XcraftPlugin extends JavaPlugin {

	@Override
	public void onDisable() {
		if (this.getConfigManager() != null) {
			this.getConfigManager().save();
		}
		this.saveConfig();
	}

	@Override
	public void onEnable() {
		setup();
	}

	protected abstract void setup();

	public abstract XcraftPluginManager getPluginManager();

	public abstract XcraftConfigManager getConfigManager();

	public abstract XcraftCommandManager getCommandManager();

	public abstract XcraftEventListener getEventListener();

	public abstract Messenger getMessenger();
}
