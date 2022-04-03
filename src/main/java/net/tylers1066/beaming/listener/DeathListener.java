package net.tylers1066.beaming.listener;

import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.localisation.I18nSupport;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class DeathListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeath(@NotNull PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (!p.hasMetadata("beaming") || !p.getMetadata("beaming").get(0).asBoolean())
            return;

        e.setDeathMessage(Beaming.PREFIX + p.getDisplayName() + " " + I18nSupport.getInternationalisedString("Beam Announcement"));
        p.setMetadata("beaming", new FixedMetadataValue(Beaming.getInstance(), false));
    }
}
