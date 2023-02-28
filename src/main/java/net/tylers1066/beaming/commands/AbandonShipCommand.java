package net.tylers1066.beaming.commands;

import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.craft.type.CraftType;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.localisation.I18nSupport;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AbandonShipCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("abandonship")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Beaming.PREFIX + I18nSupport.getInternationalisedString("Players Only"));
            return true;
        }

        if (Beaming.getInstance().getCrewCrafts().containsKey((Player)sender)) {
            Craft craft = Beaming.getInstance().getCrewCrafts().get(sender);

            if (craft instanceof PlayerCraft) {
                sender.sendMessage(Beaming.PREFIX + String.format(I18nSupport.getInternationalisedString("Abandoned Ship"), craft.getType().getStringProperty(CraftType.NAME), ((PlayerCraft) craft).getPilot().getDisplayName()));
            }
            Beaming.getInstance().getCrewCrafts().remove((Player)sender);
        } else {
            sender.sendMessage(Beaming.PREFIX + I18nSupport.getInternationalisedString("No Crew Craft Found"));
        }

        return true;
    }
}
