package de.xcraft.INemesisI.Library.Manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import de.xcraft.INemesisI.Library.XcraftPlugin;

public abstract class XcraftConfigManager {
	protected final XcraftPlugin plugin;
	protected final FileConfiguration config;

	public XcraftConfigManager(XcraftPlugin plugin) {
		this.plugin = plugin;
		File check = new File(plugin.getDataFolder(), "config.yml");
		if (!check.exists()) {
			try {
				check.createNewFile();
				plugin.saveDefaultConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		plugin.reloadConfig();
		this.config = plugin.getConfig();
	}

	public abstract void load();

	public abstract void save();

}
