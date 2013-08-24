package de.xcraft.INemesisI.Library.Message;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Library.XcraftPlugin;

public class Messenger {

	private static final Logger log = Logger.getLogger("Minecraft");
	private String prefix = "";

	public static Messenger getInstance(XcraftPlugin plugin) {
		try {
			Messenger messenger = Messenger.class.newInstance();
			messenger.setPrefix(plugin.getDescription().getName());
			return messenger;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean sendInfo(CommandSender sender, String msg, boolean showPrefix) {
		if (sender == null || msg == null || msg.equals(" ")) {
			return false;
		}
		msg = msg.replaceAll("&([0-9a-z])", "\u00a7$1");
		if (showPrefix) {
			sender.sendMessage(prefix + msg);
		} else {
			sender.sendMessage(msg);
		}
		return true;
	}

	public static boolean sendInfo(CommandSender sender, String msg, String prefix) {
		if (sender == null || msg == null || msg.equals(" ")) {
			return false;
		}
		if (!prefix.equals(""))
			prefix = ChatColor.DARK_GRAY + "[" + prefix + "] " + ChatColor.WHITE;
		msg = msg.replaceAll("&([0-9a-z])", "\u00a7$1");
		sender.sendMessage(prefix + msg);
		return true;
	}

	public static void info(String msg) {
		log.info(msg);
	}

	public static void warning(String msg) {
		log.warning(msg);
	}

	public static void severe(String msg) {
		log.severe(msg);
	}

	public void setPrefix(String prefix) {
		this.prefix = ChatColor.DARK_GRAY + "[" + prefix + "] " + ChatColor.WHITE;
	}

	public String getPrefix() {
		return prefix;
	}
}
