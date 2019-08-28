package com.github.kanesada2.MCRailways;

import org.bukkit.plugin.java.JavaPlugin;

public class MCRailways extends JavaPlugin {

	private MCRailwaysListener listener;

	@Override
    public void onEnable() {
        listener = new MCRailwaysListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
        getLogger().info("MCRailways Enabled!");
    }

    @Override
    public void onDisable() {

    }
}
