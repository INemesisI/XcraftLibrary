package de.xcraft.INemesisI.Library.Command;

import java.util.List;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;

public abstract class XcraftUsage {
	private final String name;
	private String alias;

	public XcraftUsage(String name, String alias) {
		this.name = name;
		this.alias = alias;
	}

	public abstract List<String> onTabComplete(XcraftPluginManager pManager, List<String> list, CommandSender sender, String token);

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}