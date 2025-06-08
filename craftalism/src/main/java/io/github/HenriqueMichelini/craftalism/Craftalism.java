package io.github.HenriqueMichelini.craftalism;

import io.github.HenriqueMichelini.craftalism.command.GameModeCommand;
import io.github.HenriqueMichelini.craftalism.gui.manager.GuiManager;
import io.github.HenriqueMichelini.craftalism_economy.CraftalismEconomy;
import io.github.HenriqueMichelini.craftalism_economy.economy.managers.EconomyManager;
import io.github.HenriqueMichelini.craftalism_economy.economy.util.MoneyFormat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Craftalism extends JavaPlugin {
    private static Craftalism instance;
    private static final String ECONOMY_PLUGIN_NAME = "CraftalismEconomy";
    private EconomyManager economyManager;
    private GuiManager guiManager;
    private MoneyFormat moneyFormat;

    @Override
    public void onEnable() {
        instance = this;
        this.guiManager = new GuiManager(this);

        // Plugin startup logic
        if (!setupEconomy()) {
            getLogger().severe("Disabling plugin due to missing economy dependency");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        GameModeCommand gameModeCommand = new GameModeCommand(guiManager);
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(gameModeCommand);

        getLogger().info("Craftalism has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        Plugin economyPlugin = Bukkit.getPluginManager().getPlugin(ECONOMY_PLUGIN_NAME);
        if (!(economyPlugin instanceof CraftalismEconomy)) {
            getLogger().severe(ECONOMY_PLUGIN_NAME + " not found or incompatible!");
            return false;
        }
        economyManager = ((CraftalismEconomy) economyPlugin).getEconomyManager();
        this.moneyFormat = ((CraftalismEconomy) economyPlugin).getMoneyFormat();
        return true;
    }

    public static Craftalism getInstance() {
        return instance;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }
}
