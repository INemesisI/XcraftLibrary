package de.xcraft.INemesisI.Utils;

import org.bukkit.event.Listener;

public abstract class XcraftEventListener implements Listener { 
	protected XcraftPlugin plugin;
	
	public XcraftEventListener(XcraftPlugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}
