package net.tylers1066.beaming;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(p.hasMetadata("beaming") && p.getMetadata("beaming").get(0).asBoolean()) {
            e.setDeathMessage(BeamingPlugin.PREFIX + p.getDisplayName() + " beamed to their ship");
            p.setMetadata("beaming", new FixedMetadataValue(BeamingPlugin.getInstance(),false));
        }

        if(!Config.EnableRespawn) {
            return;
        }

        if(Config.EnableRespawnMainHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setHeldItemSlot(0);
                    p.getInventory().setItemInMainHand(Config.RespawnMainHand);
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 5L);
        }
        if(Config.EnableRespawnOffHand) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.getInventory().setItemInOffHand(Config.RespawnOffHand);
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 5L);
        }
        if(Config.EnableRespawnStrength) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 600, 1));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);
        }
        if(Config.EnableRespawnSpeed) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 1));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);
        }
        if(Config.EnableRespawnResistance) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 5));
                }
            }.runTaskLater(BeamingPlugin.getInstance(), 10L);
        }
    }
}
