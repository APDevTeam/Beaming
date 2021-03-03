package net.tylers1066.beaming.commands;

import net.tylers1066.beaming.Beaming;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("beam"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Beaming.PREFIX + "Only players are allowed to use this command");
            return false;
        }

        Player player = (Player) sender;
        player.setMetadata("beaming", new FixedMetadataValue(Beaming.getInstance(),true));
        player.leaveVehicle();

        if (args.length >= 1) {
            if(args[0].equalsIgnoreCase("scotty")) {
                player.chat("Beam me up scotty!");
            }
            else if(args[0].equalsIgnoreCase("picard")) {
                player.chat("Energize!");
            }
            else {
                player.sendMessage(Beaming.PREFIX + "Invalid argument");
            }
        }

        player.sendMessage(Beaming.PREFIX + "You beamed to your ship!");

        player.setHealth(0);
        player.spigot().respawn();
        return true;
    }
}
