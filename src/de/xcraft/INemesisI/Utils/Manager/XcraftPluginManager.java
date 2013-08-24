package de.xcraft.INemesisI.Utils.Manager;

import de.xcraft.INemesisI.Utils.XcraftPlugin;

public abstract class XcraftPluginManager {

	public XcraftPlugin plugin;

	public XcraftPluginManager(XcraftPlugin plugin) {
		this.plugin = plugin;
	}

	public abstract XcraftPlugin getPlugin();
}
