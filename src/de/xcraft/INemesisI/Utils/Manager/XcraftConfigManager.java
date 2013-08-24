package de.xcraft.INemesisI.Utils.Manager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import de.xcraft.INemesisI.Utils.XcraftPlugin;

public abstract class XcraftConfigManager {
	protected final XcraftPlugin plugin;
	protected final FileConfiguration config;
	private boolean loaded = false;;

	public XcraftConfigManager(XcraftPlugin plugin) {
		this.plugin = plugin;
		File check = new File(plugin.getDataFolder(), "config.yml");
		if (!check.exists()) {
			plugin.saveDefaultConfig();
			try {
				check.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		plugin.reloadConfig();
		this.config = plugin.getConfig();
		loaded = true;
	}

	public abstract void load();

	public abstract void save();

	public boolean isLoaded() {
		return loaded;
	}

}
