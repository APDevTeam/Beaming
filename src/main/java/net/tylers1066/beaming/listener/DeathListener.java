package net.tylers1066.beaming.listener;

import net.tylers1066.beaming.Beaming;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(p.hasMetadata("beaming") && p.getMetadata("beaming").get(0).asBoolean()) {
            e.setDeathMessage(Beaming.PREFIX + p.getDisplayName() + " beamed to their ship");
            p.setMetadata("beaming", new FixedMetadataValue(Beaming.getInstance(),false));
        }
    }
}
