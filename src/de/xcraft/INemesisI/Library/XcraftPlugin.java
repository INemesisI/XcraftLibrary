package de.xcraft.INemesisI.Library;

import org.bukkit.plugin.java.JavaPlugin;

import de.xcraft.INemesisI.Library.Manager.XcraftCommandManager;
import de.xcraft.INemesisI.Library.Manager.XcraftConfigManager;
import de.xcraft.INemesisI.Library.Manager.XcraftPluginManager;
import de.xcraft.INemesisI.Library.Message.Messenger;

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
