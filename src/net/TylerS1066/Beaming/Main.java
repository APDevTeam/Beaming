package net.TylerS1066.Beaming;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin
{
  	@Override
    public void onEnable()
    {
  		
    }
   
    @Override
    public void onDisable()
    {
       
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("beam"))
        {
        	if(!(sender instanceof Player))
            {
                sender.sendMessage("Only players are allowed to use this command");
                return false;
            }
            else
            {
                Player player = (Player) sender;
                player.setHealth(0);
                //force player to respawn
                sender.sendMessage("[Beaming] You beamed to your ship!");
                String message = "[Beaming]" + player.getDisplayName() + " beamed to their ship";
                Bukkit.broadcastMessage(message);
            }
        	return true;
        }
        return false;
    }
}
