package de.xcraft.INemesisI.Library.Manager;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import de.xcraft.INemesisI.Library.XcraftPlugin;
import de.xcraft.INemesisI.Library.Message.Messenger;

public abstract class XcraftConfigManager {
	protected final XcraftPlugin plugin;
	protected final FileConfiguration config;

	public XcraftConfigManager(XcraftPlugin plugin) {
		this.plugin = plugin;
		File check = new File(plugin.getDataFolder(), "config.yml");
		if (!check.exists()) {
			Messenger.info("[" + plugin.getName() + "] Creating new config folder");
			plugin.saveDefaultConfig();
		}
		plugin.reloadConfig();
		this.config = plugin.getConfig();
	}

	public abstract void load();

	public abstract void save();

}
