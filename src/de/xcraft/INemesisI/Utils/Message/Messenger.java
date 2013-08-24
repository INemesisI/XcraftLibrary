package de.xcraft.INemesisI.Utils.Message;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.xcraft.INemesisI.Utils.XcraftPlugin;

public class Messenger {

	private static final Logger log = Logger.getLogger("Minecraft");
	private static String prefix = "";
	
	public static Messenger getInstance(XcraftPlugin plugin) {
		prefix = ChatColor.DARK_GRAY + "[" + plugin.getName() + "] "+ ChatColor.WHITE;
		try {
			return Messenger.class.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
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
		prefix = ChatColor.DARK_GRAY + "[" + prefix + "] " + ChatColor.WHITE;
		msg = msg.replaceAll("&([0-9a-z])", "\u00a7$1");
		sender.sendMessage(prefix + msg);
		return true;
	}


	public static void info(String msg) {
		log.info(prefix + msg);
	}

	public static void warning(String msg) {
		log.warning(prefix + msg);
	}

	public static void severe(String msg) {
		log.severe(prefix + msg);
	}
}
