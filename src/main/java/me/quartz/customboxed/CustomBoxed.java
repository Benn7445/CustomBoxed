package me.quartz.customboxed;

import me.quartz.customboxed.commands.IslandCommand;
import me.quartz.customboxed.files.FileManager;
import me.quartz.customboxed.plot.PlotManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomBoxed extends JavaPlugin {

    private static CustomBoxed instance;
    private PlotManager plotManager;
    private FileManager fileManager;
    private Economy econ;

    public static CustomBoxed getInstance() {
        return instance;
    }

    public PlotManager getPlotManager() {
        return plotManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public Economy getEcon() {
        return econ;
    }

    @Override
    public void onEnable() {
        registerManagers();
        registerCommands();
        registerCheckedEconomy();
    }

    @Override
    public void onDisable() {

    }

    private void registerManagers() {
        instance = this;
        fileManager = new FileManager();
        plotManager = new PlotManager();
    }

    private void registerCommands() {
        getCommand("island").setExecutor(new IslandCommand());
    }

    private void registerCheckedEconomy() {
        if (!registerEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
}
