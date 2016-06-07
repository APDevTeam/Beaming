package net.TylerS1066.Beaming;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {

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
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("scotty"))
						this.getServer().broadcastMessage(player.getDisplayName() + ": 'Beam me up scotty!'");
				} else {
					sender.sendMessage("§1[§eBeaming§1] §cYou beamed to you ship!");
					String message = "§1[§eBeaming§1]§c" + player.getDisplayName() + "§r§c beamed to their ship";
					Bukkit.broadcastMessage(message);
				}
				player.getWorld().setGameRuleValue("showDeathMessages" , "false");
				player.setHealth(0);
				player.spigot().respawn();
				String message = "§1[§eBeaming§1]§c" + player.getDisplayName() + "§r§c beamed to their ship";
				Bukkit.broadcastMessage(message);
				player.getWorld().setGameRuleValue("showDeathMessages" , "true");
			}
			return true;
		}
		return false;
	}


}