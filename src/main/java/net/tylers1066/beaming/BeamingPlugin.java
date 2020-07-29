package net.tylers1066.beaming;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;

public class BeamingPlugin extends JavaPlugin implements Listener{
    public final static String PREFIX = ChatColor.DARK_BLUE + "[" + ChatColor.YELLOW + "Beaming" + ChatColor.DARK_BLUE + "] " + ChatColor.RED;
    private static BeamingPlugin instance;

    static BeamingPlugin getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        this.getCommand("beam").setExecutor(new BeamingCommand());
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {

    }
}