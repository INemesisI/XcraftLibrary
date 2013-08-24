package de.xcraft.INemesisI.Library.Command;

import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;

public abstract class XcraftCommand {
	private final XcraftCommandInfo commandInfo;

	public XcraftCommand(String command, String name, String pattern, String usage, String desc, String permission) {
		this.commandInfo = new XcraftCommandInfo(command, name, pattern, usage, desc, permission);
	}

	public XcraftCommandInfo getCommandInfo() {
		return commandInfo;
	}

	public abstract boolean execute(XcraftPluginManager manager, CommandSender sender, String[] args);

}