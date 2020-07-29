package net.tylers1066.beaming;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BeamingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("beam"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(BeamingPlugin.PREFIX + "Only players are allowed to use this command");
            return false;
        }

        Player player = (Player) sender;
        player.setMetadata("beaming", new FixedMetadataValue(BeamingPlugin.getInstance(),true));
        player.leaveVehicle();

        if (args.length >= 1) {
            if(args[0].equalsIgnoreCase("scotty")) {
                player.chat("Beam me up scotty!");
            }
            else if(args[0].equalsIgnoreCase("picard")) {
                player.chat("Energize!");
            }
            else {
                player.sendMessage(BeamingPlugin.PREFIX + "Invalid argument");
            }
        }

        player.sendMessage(BeamingPlugin.PREFIX + "You beamed to your ship!");

        player.setHealth(0);
        player.spigot().respawn();

        return true;
    }
}
