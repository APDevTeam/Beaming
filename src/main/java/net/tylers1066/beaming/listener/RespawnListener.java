package net.tylers1066.beaming.listener;

import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class RespawnListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onRespawn(@NotNull PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (!Config.EnableRespawn)
            return;

        if (Config.EnableRespawnMainHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setHeldItemSlot(0);
                    p.getInventory().setItemInMainHand(Config.RespawnMainHand);
                }
            }.runTaskLater(Beaming.getInstance(), 5L);
        }
        if (Config.EnableRespawnOffHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setItemInOffHand(Config.RespawnOffHand);
                }
            }.runTaskLater(Beaming.getInstance(), 5L);
        }
        if (Config.EnableRespawnStrength) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Config.StrengthDuration, Config.StrengthAmplitude));
                }
            }.runTaskLater(Beaming.getInstance(), 10L);
        }
        if (Config.EnableRespawnSpeed) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Config.SpeedDuration, Config.SpeedAmplitude));
                }
            }.runTaskLater(Beaming.getInstance(), 10L);
        }
        if (Config.EnableRespawnResistance) {
            p.setMetadata("BeamingRespawn", new FixedMetadataValue(Beaming.getInstance(), null));

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Config.ResistanceDuration, Config.ResistanceAmplitude));
                }
            }.runTaskLater(Beaming.getInstance(), 10L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.removeMetadata("BeamingRespawn", Beaming.getInstance());
                }
            }.runTaskLater(Beaming.getInstance(), 10L + Config.ResistanceDuration);
        }

    }
}
