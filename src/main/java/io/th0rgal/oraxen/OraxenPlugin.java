package io.th0rgal.oraxen;

import io.th0rgal.oraxen.commands.BaseCommand;
import io.th0rgal.oraxen.commands.CommandHandler;
import io.th0rgal.oraxen.commands.subcommands.Debug;
import io.th0rgal.oraxen.commands.subcommands.Give;
import io.th0rgal.oraxen.commands.subcommands.InventoryVisualizer;
import io.th0rgal.oraxen.items.OraxenItems;
import io.th0rgal.oraxen.items.mechanics.MechanicsManager;
import io.th0rgal.oraxen.listeners.EventsManager;
import io.th0rgal.oraxen.utils.Logs;
import io.th0rgal.oraxen.utils.pack.ResourcePack;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class OraxenPlugin extends JavaPlugin {

    private static OraxenPlugin instance;

    public OraxenPlugin() throws Exception {
        instance = this;
        Logs.enableFilter();
    }

    public void registerCommands() {
        CommandHandler handler = new CommandHandler();
        handler.register("oraxen", new BaseCommand());
        handler.register("debug", new Debug());
        handler.register("inv", new InventoryVisualizer());
        handler.register("give", new Give());
        getCommand("oraxen").setExecutor(handler);
    }

    public void onEnable() {
        MechanicsManager.registerNativeMechanics();
        OraxenItems.loadItems();
        new ResourcePack().generate();
        registerCommands();
        Logs.log(ChatColor.GREEN + "Successfully loaded");
        new EventsManager(this).registerEvents();
    }

    public void onDisable() {
        Logs.log(ChatColor.GREEN + "Successfully unloaded");
    }

    public static OraxenPlugin get() {
        return instance;
    }

}