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
	}

	@Override
	public void onEnable() {
		String[] version = this.getServer().getPluginManager().getPlugin("XcraftLibrary").getDescription().getVersion().split("\\.");
		String[] required = (this.getDescription().getDescription().split("v")[1]).split("\\.");
		for (int i = 0; i < 3; i++) {
			if (Integer.parseInt(version[i]) < Integer.parseInt(required[i])) {
				Messenger.severe("-------------------------------------------------------------------");
				Messenger.severe(this.getName() + ": The version of XcraftLibrary is not compatible with the Plugin version!");
				Messenger.severe(this.getDescription().getDescription());
				Messenger.severe("-------------------------------------------------------------------");
				return;
			}
		}
		setup();
	}

	protected abstract void setup();

	public abstract XcraftPluginManager getPluginManager();

	public abstract XcraftConfigManager getConfigManager();

	public abstract XcraftCommandManager getCommandManager();

	public abstract XcraftEventListener getEventListener();

	public abstract Messenger getMessenger();
}
