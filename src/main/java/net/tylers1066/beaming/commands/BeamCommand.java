package net.tylers1066.beaming.commands;

import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.localisation.I18nSupport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class BeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("beam"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(Beaming.PREFIX + I18nSupport.getInternationalisedString("Players Only"));
            return false;
        }

        Player player = (Player) sender;
        player.setMetadata("beaming", new FixedMetadataValue(Beaming.getInstance(), true));
        player.leaveVehicle();

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("scotty"))
                player.chat(I18nSupport.getInternationalisedString("Scotty"));
            else if (args[0].equalsIgnoreCase("picard"))
                player.chat(I18nSupport.getInternationalisedString("Picard"));
            else
                player.sendMessage(Beaming.PREFIX + I18nSupport.getInternationalisedString("Invalid Argument"));
        }

        player.sendMessage(Beaming.PREFIX + " " + I18nSupport.getInternationalisedString("Beam Message"));

        player.setHealth(0);
        player.spigot().respawn();
        return true;
    }
}
