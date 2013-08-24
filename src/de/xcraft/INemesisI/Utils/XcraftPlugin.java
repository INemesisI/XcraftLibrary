package de.xcraft.INemesisI.Utils;

import org.bukkit.plugin.java.JavaPlugin;

import de.xcraft.INemesisI.Utils.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Utils.Manager.XcraftConfigManager;
import de.xcraft.INemesisI.Utils.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Utils.Message.Messenger;

public abstract class XcraftPlugin extends JavaPlugin {
	public XcraftPluginManager pluginManager;
	public XcraftConfigManager configManager;
	public XcraftCommandManager commandManager;
	public XcraftEventListener eventListener;
	public Messenger messenger = null;

	@Override
	public void onDisable() {
		configManager.save();
	}

	@Override
	public void onEnable() {
		this.messenger = Messenger.getInstance(this);
		setup();
	}
	protected abstract void setup();
}
