package de.xcraft.INemesisI.Library.Command;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;

public abstract class XcraftCommand {
	private final XcraftCommandManager cManager;
	private final String bukkitCommand;
	private final String name;
	private final String pattern;
	private String usage;
	private String desc;
	private final String permission;

	public XcraftCommand(XcraftCommandManager cManager, String command, String name, String pattern, String usage, String desc, String permission) {
		this.cManager = cManager;
		this.bukkitCommand = command;
		this.name = name;
		this.pattern = pattern;
		this.usage = usage;
		this.desc = desc;
		this.permission = permission;
	}

	public void addCommandShortcut(XcraftCommandManager manager, String shortcut) {
		return;
	}

	public abstract boolean execute(XcraftPluginManager manager, CommandSender sender, String[] args);

	protected void sendInfo(CommandSender sender, String msg, boolean showPrefix) {
		cManager.getPlugin().getMessenger().sendInfo(sender, msg, showPrefix);
		return;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBukkitCommand() {
		return bukkitCommand;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}

	public String getPermission() {
		return permission;
	}

}