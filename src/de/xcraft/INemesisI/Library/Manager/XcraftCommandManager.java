package de.xcraft.INemesisI.Library.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.xcraft.INemesisI.Library.XcraftPlugin;
import de.xcraft.INemesisI.Library.Command.XcraftCommand;
import de.xcraft.INemesisI.Library.Command.XcraftUsage;
import de.xcraft.INemesisI.Library.Message.Messenger;

public abstract class XcraftCommandManager implements CommandExecutor, TabCompleter {

	protected final XcraftPlugin plugin;
	protected final Map<String, XcraftCommand> commands = new TreeMap<String, XcraftCommand>();
	protected final Map<String, XcraftUsage> usages = new HashMap<String, XcraftUsage>();

	public XcraftCommandManager(XcraftPlugin plugin) {
		this.plugin = plugin;
		registerCommands();
	}

	protected abstract void registerCommands();

	protected void registerBukkitCommand(String bcmd) {
		if (plugin.getCommand(bcmd) == null)
			Messenger.severe("The command '" + bcmd + "' was not defined in the plugin.yml!");
		else
			plugin.getCommand(bcmd).setExecutor(this);
	}

	public void registerCommand(XcraftCommand command) {
		try {
			commands.put(command.getPattern(), command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerUsage(XcraftUsage usage) {
		usages.put(usage.getName(), usage);
	}

	public Map<String, XcraftCommand> getCommands() {
		return commands;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command bcmd, String label, String[] args) {
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
		for (Entry<String, XcraftCommand> commandEntry : commands.entrySet()) {
			if (cmd.matches(commandEntry.getKey()) && bcmd.getName().equals(commandEntry.getValue().getCommand())) {
				if (commandEntry.getValue().getName().equals(cmd)) {
					matches.clear();
					matches.add(commandEntry.getValue());
					break;
				} else {
					matches.add(commandEntry.getValue());
				}
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
			Messenger.sendInfo(sender, ChatColor.RED + "Unknown command: '" + cmd + "'!", plugin.getDescription().getName());
			this.showHelp(sender, bcmd.getName());
			return true;
		}
		// Grab the only match.
		XcraftCommand command = matches.get(0);
		// First check if the sender has permission.
		if (!sender.hasPermission(command.getPermission())) {
			Messenger.sendInfo(sender, ChatColor.RED + "You don't have access to this command.", plugin.getDescription().getName());
			return true;
		}
		// Then Check if the sender used the command right
		if (!validateUsage(sender, command.getUsage().replace("...", "").split(" "), 0, args, 1)) {
			showUsage(sender, command);
			return true;
		}
		// Otherwise, execute the command!
		if (!command.execute(plugin.pluginManager, sender, Arrays.copyOfRange(args, 1, args.length)))
			showUsage(sender, command);
		return true;
	}

	private boolean validateUsage(CommandSender sender, String[] commandUsage, int c, String[] params, int p) {
		if (commandUsage.length > c) {
			String usageName = commandUsage[c].substring(1, commandUsage[c].length() - 1);
			if (params.length > p) {
				if (usages.containsKey(usageName)) {
					if (usages.get(usageName).validate(params[p])) {
							return validateUsage(sender, commandUsage, c + 1, params, p + 1);
					} else {
						if (commandUsage[c].startsWith("[")) {
							return validateUsage(sender, commandUsage, c + 1, params, p);
						} else {
							Messenger.sendInfo(sender, usages.get(usageName).getFailMessage(), plugin.getDescription().getName());
							return false;
						}
					}
				} else {
					if (params.equals(usageName)) {
						return validateUsage(sender, commandUsage, c + 1, params, p + 1);
					}
				}
			} else if (usages.containsKey(usageName)) {
				Messenger.sendInfo(sender, usages.get(usageName).getFailMessage(), plugin.getDescription().getName());
				return false;
			} else {
				Messenger.sendInfo(sender, ChatColor.RED + "Wrong count of arugments", plugin.getDescription().getName());
				return false;
			}
		}
		return true;
	}

	private void showHelp(CommandSender sender, String cmd) {
		Messenger.sendInfo(sender, ChatColor.GOLD + plugin.getDescription().getVersion() + " By " + plugin.getDescription().getAuthors() + ":",
				plugin.getDescription().getName());
		for (XcraftCommand command : commands.values()) {
			if (cmd.matches(command.getCommand())) {
				this.showUsage(sender, command);
			}
		}
	}

	private void showUsage(CommandSender sender, XcraftCommand command) {
		if (!sender.hasPermission(command.getPermission())) {
			return;
		}
		String usage = command.getUsage();
		for (String usageName : usages.keySet()) {
			if (usage.contains(usageName)) {
				usage = usage.replace(usageName, usages.get(usageName).getAlias());
			}
		}
		Messenger.sendInfo(sender, "&8->&a/" + command.getCommand() + " " + command.getName() + " " + usage + " &3- " + command.getDesc(), "");
	}

	public void onReload(CommandSender sender) {
		if (sender.hasPermission(plugin.getName() + ".Reload")) {
			plugin.reloadConfig();
			plugin.configManager.config = plugin.getConfig();
			plugin.configManager.load();
			Messenger.sendInfo(sender, "loaded all data from disc!", plugin.getDescription().getName());
			Messenger.info(plugin.getDescription().getName() + " manual reload!");
		}
	}

	public void onSave(CommandSender sender) {
		if (sender.hasPermission(plugin.getName() + ".Save")) {
			plugin.configManager.save();
			plugin.saveConfig();
			Messenger.sendInfo(sender, "Saved all data to disc!", plugin.getDescription().getName());
			Messenger.info(plugin.getDescription().getName() + " manual save!");
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command bcmd, String alias, String[] args) {
		System.out.println("test");
		List<String> list = new ArrayList<String>();
		for (XcraftCommand command : plugin.commandManager.getCommands().values()) {
			if (bcmd.getName().matches(command.getCommand()) && sender.hasPermission(command.getPermission())) {
				// Found the right command
				System.out.println("!");
				if (args.length > 1 && args[0].matches(command.getPattern())) {
					String[] usages = command.getUsage().split(" ");
					String token = args[args.length - 1].trim().toLowerCase();
					getUsageList(sender, list, usages, token, args.length - 2);

					break;
				} else if (args.length <= 1 && (args[0].equals("") || command.getName().startsWith(args[0]))) {
					list.add(command.getName());
				}
			}
		}
		return list;
	}

	private List<String> getUsageList(CommandSender sender, List<String> list, String[] usages, String token, int a) {
		// if the usage ends with '...' it will continously use the usage before
		// '...' afterwards
		if (a >= usages.length - 1 && usages[usages.length - 1].equals("...") && !usages[usages.length - 2].startsWith("[")) {
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

	protected List<String> onTabComplete(List<String> list, CommandSender sender, String usage, String token) {
		boolean foundUsage = false;
		for (String usageName : usages.keySet()) {
			if (usage.equals(usageName)) {
				usages.get(usageName).onTabComplete(list, sender, token);
				foundUsage = true;
			}
		}
		if (!foundUsage)
			list.add(usage);
		return list;
	}

}
