package net.TylerS1066.Beaming;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Main extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("beam")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players are allowed to use this command");
                return false;
            } else {
                Player player = (Player) sender;
                player.setMetadata("beaming", new FixedMetadataValue(this,true));
                player.leaveVehicle();
                if (args.length >= 1 && args[0].equalsIgnoreCase("scotty")) {
                    this.getServer().broadcastMessage(player.getDisplayName() + ": 'Beam me up scotty!'");
                }
                sender.sendMessage("§1[§eBeaming§1] §cYou beamed to your ship!");
                player.setHealth(0);
                player.spigot().respawn();
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(p.hasMetadata("beaming") && p.getMetadata("beaming").get(0).asBoolean() ){
                e.setDeathMessage("§1[§eBeaming§1]§c" + p.getDisplayName() + "§r§c beamed to their ship");
                p.setMetadata("beaming", new FixedMetadataValue(this,false));
            }
        }
    }
}