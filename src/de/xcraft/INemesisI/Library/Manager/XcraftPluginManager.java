package de.xcraft.INemesisI.Library.Manager;

import de.xcraft.INemesisI.Library.XcraftPlugin;

public abstract class XcraftPluginManager {

	public XcraftPlugin plugin;

	public XcraftPluginManager(XcraftPlugin plugin) {
		this.plugin = plugin;
	}

	public abstract XcraftPlugin getPlugin();
}
