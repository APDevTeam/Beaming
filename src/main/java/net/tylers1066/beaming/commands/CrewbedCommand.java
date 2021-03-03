package net.tylers1066.beaming.commands;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.localisation.I18nSupport;
import net.tylers1066.beaming.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrewbedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(!cmd.getName().equalsIgnoreCase("crewbed"))
            return false;

        if(!sender.hasPermission("beaming.crewbed"))
            return false;

        if(!(sender instanceof Player)) {
            sender.sendMessage(Beaming.PREFIX + I18nSupport.getInternationalisedString("CrewBed - Must Be Player"));
            return true;
        }

        Player player = (Player) sender;
        Location respawnLoc = getActiveCrewSign(player);
        if(respawnLoc != null) {
            sender.sendMessage(I18nSupport.getInternationalisedString("CrewBed - Current Location") + String.format(": %d, %d, %d", respawnLoc.getBlockX(), respawnLoc.getBlockY(), respawnLoc.getBlockZ()));
            return true;
        }

        Location bedLocation = player.getBedLocation();
        sender.sendMessage(I18nSupport.getInternationalisedString("CrewBed - Priority Location") + String.format(": %d, %d, %d", bedLocation.getBlockX(), bedLocation.getBlockY(), bedLocation.getBlockZ()));
        return true;
    }

    @Nullable
    private Location getActiveCrewSign(Player player) {
        Craft craft = CraftManager.getInstance().getCraftByPlayer(player);
        if(craft == null)
            return null;

        Location sign = Utils.getCrewSign(craft);
        if(sign == null)
            return null;

        return Utils.getRespawnLocation(sign);
    }
}
