package net.tylers1066.beaming.sign;

import com.earth2me.essentials.User;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.craft.type.CraftType;
import net.countercraft.movecraft.events.ManOverboardEvent;
import net.countercraft.movecraft.util.MathUtils;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;
import net.tylers1066.beaming.localisation.I18nSupport;
import net.tylers1066.beaming.utils.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CrewSign implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public final void onSignChange(@NotNull SignChangeEvent event) {
        if (!event.getLine(0).equalsIgnoreCase("Crew:"))
            return;

        Player player = event.getPlayer();
        event.setLine(1, player.getName());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public final void onSignRightClick(@NotNull PlayerInteractEvent event) {
        if (!Config.EnableCrewSigns || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;


        Player player = event.getPlayer();
        if (!player.isSneaking())
            return;

        if (event.getClickedBlock() == null || !(event.getClickedBlock().getState() instanceof Sign))
            return;

        Sign sign = (Sign) event.getClickedBlock().getState();
        if (!sign.getLine(0).equalsIgnoreCase("Crew:"))
            return;

        if (!Tag.BEDS.getValues().contains(sign.getBlock().getRelative(0, -1, 0).getType())) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Need Bed Below"));
            return;
        }

        if (CraftManager.getInstance().getCraftByPlayer(player) != null) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Craft Currently Piloted"));
            return;
        }

        Location location = sign.getLocation();
        MovecraftLocation movecraftLocation = MathUtils.bukkit2MovecraftLoc(location);
        for (Craft craft : CraftManager.getInstance().getCraftsInWorld(location.getWorld())) {
            if (!(craft instanceof PlayerCraft)) continue;
            if (craft.getHitBox().contains(movecraftLocation)) {
                HashMap crewMap = Beaming.getInstance().getCrewCrafts();
                if (crewMap.containsKey(player)) {
                    crewMap.remove(player);
                }
                crewMap.put(player, craft);
                player.sendMessage(Beaming.PREFIX + String.format(I18nSupport.getInternationalisedString("Joined Ship"), craft.getType().getStringProperty(CraftType.NAME), ((PlayerCraft) craft).getPilot().getDisplayName()));
                return;
            }
        }

        if (!sign.getLine(1).equalsIgnoreCase(player.getName())) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Sign Not Owned"));
            return;
        }
        
        player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Spawn Set"));
        player.setBedSpawnLocation(location, true);
        sign.setLine(0, ChatColor.BOLD + "Crew:");

        if (!Config.SetHomeToCrewSign || Beaming.getInstance().getEssentials() == null)
            return;

        User u = Beaming.getInstance().getEssentials().getUser(player);
        u.setHome("home", location);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Craft c;
        if (Beaming.getInstance().getCrewCrafts().containsKey(p)) {
            c = Beaming.getInstance().getCrewCrafts().get(p);
            if (c instanceof SinkingCraft || c.getDisabled()) {
                Beaming.getInstance().getCrewCrafts().remove(p);
                c = CraftManager.getInstance().getCraftByPlayer(p);
            }
        } else {
            c = CraftManager.getInstance().getCraftByPlayer(p);
        }
        if (c == null)
            return;
        if (c instanceof SinkingCraft || c.getDisabled())
            return;

        Location sign = Utils.getCrewSign(c);
        if (sign == null)
            return;

        Location respawn = Utils.getRespawnLocation(sign);
        if (respawn == null)
            return;

        e.setRespawnLocation(respawn);
        p.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Respawn"));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onManOverboard(@NotNull ManOverboardEvent e) {
        Location sign = Utils.getCrewSign(e.getCraft());
        if (sign == null)
            return;

        Location respawn = Utils.getRespawnLocation(sign);
        if (respawn == null)
            return;

        e.setLocation(respawn);
    }
}
