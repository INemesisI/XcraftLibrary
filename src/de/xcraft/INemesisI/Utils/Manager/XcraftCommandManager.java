package de.xcraft.INemesisI.Utils.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.xcraft.INemesisI.Utils.XcraftPlugin;
import de.xcraft.INemesisI.Utils.Command.XcraftCommand;
import de.xcraft.INemesisI.Utils.Command.XcraftCommandInfo;
import de.xcraft.INemesisI.Utils.Command.XcraftUsage;
import de.xcraft.INemesisI.Utils.Message.Messenger;

public abstract class XcraftCommandManager implements CommandExecutor, TabCompleter {

	protected final XcraftPlugin plugin;
	protected final Map<String, XcraftCommand> commands = new TreeMap<String, XcraftCommand>();
	protected final List<XcraftUsage> usages = new ArrayList<XcraftUsage>();

	public XcraftCommandManager(XcraftPlugin plugin) {
		this.plugin = plugin;
		registerCommands();
	}

	protected abstract void registerCommands();

	protected void registerBukkitCommand(String command) {
		if (plugin.getCommand(command) == null)
			Messenger.severe("The command '" + command + "' was not defined in the plugin.yml!");
		else
			plugin.getCommand(command).setExecutor(this);
	}

	public void registerCommand(XcraftCommand cmd) {
		try {
			commands.put(cmd.getCommandInfo().getPattern(), cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerUsage(XcraftUsage usage) {
		usages.add(usage);
	}

	public Map<String, XcraftCommand> getCommands() {
		return commands;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command bcmd, String label,
			String[] args) {
		// Grab the base and arguments.
		String cmd = (args.length > 0 ? args[0] : "");
		// The help command is a little special
		if (cmd.equals("") || cmd.equals("?") || cmd.equals("help")) {
			this.showHelp(sender, bcmd.getName());
			return true;
		}
		if (cmd.equals("save")) {
			onSave(sender);
			return true;
		}
		if (cmd.equals("reload")) {
			onReload(sender);
			return true;
		}
		// Get all commands that match the base.
		List<XcraftCommand> matches = new ArrayList<XcraftCommand>();
		// Grab the commands that match the argument.
		for (Entry<String, XcraftCommand> entry : commands.entrySet()) {
			if (cmd.matches(entry.getKey())
					&& cmd.matches(entry.getValue().getCommandInfo().getPattern())) {
				matches.add(entry.getValue());
			}
		}
		// If there's more than one match, display them.
		if (matches.size() > 1) {
			Messenger.sendInfo(sender, "Available commands:", plugin.getDescription().getName());
			for (XcraftCommand c : matches) {
				this.showUsage(sender, c);
			}
			return true;
		}
		// If there are no matches at all, notify.
		if (matches.size() == 0) {
			Messenger.sendInfo(sender, ChatColor.RED + "Unknown command: '" + cmd + "'!", plugin
					.getDescription().getName());
			this.showHelp(sender, bcmd.getName());
			return true;
		}
		// Grab the only match.
		XcraftCommand command = matches.get(0);
		// First check if the sender has permission.
		if (!sender.hasPermission(command.getCommandInfo().getPermission())) {
			Messenger.sendInfo(sender, ChatColor.RED + "You don't have access to this command.",
					plugin.getDescription().getName());
			return true;
		}
		String[] params = Arrays.copyOfRange(args, 1, args.length);
		if (command.getCommandInfo().getUsage().split("<|\\[").length - 1 > params.length) {
			Messenger.sendInfo(sender, ChatColor.RED + "Wrong count of arugments", plugin
					.getDescription().getName());
			showUsage(sender, command);
			return true;
		}
		// Otherwise, execute the command!
		if (!command.execute(plugin.pluginManager, sender, params))
			showUsage(sender, command);
		return true;
	}

	private void showHelp(CommandSender sender, String cmd) {
		Messenger.sendInfo(sender, ChatColor.GOLD + plugin.getDescription().getVersion()
				+ " By INemesisI :", plugin.getDescription().getName());
		for (XcraftCommand command : commands.values()) {
			if (cmd.matches(command.getCommandInfo().getCommand())) {
				this.showUsage(sender, command);
			}
		}
	}

	private void showUsage(CommandSender sender, XcraftCommand cmd) {
		XcraftCommandInfo info = cmd.getCommandInfo();
		if (!sender.hasPermission(info.getPermission())) {
			return;
		}
		String usage = info.getUsage();
		for (XcraftUsage u : usages) {
			if (usage.contains(u.getName())) {
				usage = usage.replace(u.getName(), u.getAlias());
			}
		}
		Messenger.sendInfo(sender, "&8->&a/" + info.getCommand() + " " + info.getName() + " "
				+ usage + " &3- " + info.getDesc(), "");
	}

	public void onReload(CommandSender sender) {
		plugin.configManager.load();
		Messenger.sendInfo(sender, "loaded all data from disc!", plugin.getDescription().getName());
		Messenger.info(plugin.getDescription().getName() + " manual reload!");
	}

	public void onSave(CommandSender sender) {
		plugin.configManager.save();
		Messenger.sendInfo(sender, "Saved all data to disc!", plugin.getDescription().getName());
		Messenger.info(plugin.getDescription().getName() + " manual save!");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bcmd,
			String alias, String[] args) {
		List<String> list = new ArrayList<String>();
		for (XcraftCommand cmd : plugin.commandManager.getCommands().values()) {
			XcraftCommandInfo commandInfo = cmd.getCommandInfo();
			if (bcmd.getName().matches(commandInfo.getCommand())
					&& sender.hasPermission(commandInfo.getPermission())) {
				// Found the right command
				if (args.length > 1 && args[0].matches(commandInfo.getPattern())) {
					String[] usages = commandInfo.getUsage().split(" ");
					String token = args[args.length - 1].trim().toLowerCase(); // what
																				// the
																				// user
																				// typed
																				// in
																				// so
																				// far
					getUsageList(sender, list, usages, token, args.length - 2);

					break;
				} else if (args.length <= 1
						&& (args[0].equals("") || commandInfo.getName().startsWith(args[0]))) {
					list.add(commandInfo.getName());
				}
			}
		}
		return list;
	}

	private List<String> getUsageList(CommandSender sender, List<String> list, String[] usages,
			String token, int a) {
		// if the usage ends with '...' it will continously use the usage before
		// '...' afterwards
		if (a >= usages.length - 1 && usages[usages.length - 1].equals("...")
				&& !usages[usages.length - 2].startsWith("[")) {
			return getUsageList(sender, list, usages, token, usages.length - 2);
		} else if (a >= usages.length) {
			return list;
		}
		if (usages[a].startsWith("<") && usages[a].endsWith(">")) {
			String usage = usages[a].substring(1, usages[a].length() - 1);
			if (usage.contains("/")) {
				for (String u : usage.split("/")) {
					onTabComplete(list, sender, u, token);
				}
				return list;
			} else {
				onTabComplete(list, sender, usage, token);
			}
			return list;
		} else if (usages[a].startsWith("[") && usages[a].endsWith("]")) {
			String usage = usages[a].substring(1, usages[a].length() - 1);
			if (usage.contains("/")) {
				for (String u : usage.split("/")) {
					onTabComplete(list, sender, u, token);
				}
				return list;
			} else {
				onTabComplete(list, sender, usage, token);
			}
			return getUsageList(sender, list, usages, token, a + 1);
		} else
			return list;
	}

	protected List<String> onTabComplete(List<String> list, CommandSender sender, String usage,
			String token) {
		boolean foundUsage = false;
		for (XcraftUsage u : usages) {
			if (usage.equals(u.getName())) {
				u.onTabComplete(plugin.pluginManager, list, sender, token);
				foundUsage = true;
			}
		}
		if (!foundUsage)
			list.add(usage);
		return list;
	}

}
