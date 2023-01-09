package net.tylers1066.beaming.sign;

import com.earth2me.essentials.User;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.ManOverboardEvent;
import net.countercraft.movecraft.events.SignTranslateEvent;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;
import net.tylers1066.beaming.localisation.I18nSupport;
import net.tylers1066.beaming.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        if (!sign.getLine(1).equalsIgnoreCase(player.getName())) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Sign Not Owned"));
            return;
        }
        if (CraftManager.getInstance().getCraftByPlayer(player) != null) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Craft Currently Piloted"));
            return;
        }

        Location location = sign.getLocation();
        player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Spawn Set"));
        player.setBedSpawnLocation(location, true);
        sign.setLine(0, ChatColor.BOLD + "Crew:");

        if (!Config.SetHomeToCrewSign || Beaming.getInstance().getEssentials() == null)
            return;

        User u = Beaming.getInstance().getEssentials().getUser(player);
        u.setHome("home", location);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSignTranslate(@NotNull SignTranslateEvent event) {
        if (!Config.UpdateBedLocations || !Utils.isCrewSign(event::getLine))
            return;

        World w = event.getCraft().getWorld();
        for (MovecraftLocation l : event.getLocations()) {
            Location signLocation = l.toBukkit(w);

            Block bed = Utils.getBedBlock(signLocation);
            if (!Utils.isBed(bed))
                continue;

            Player player = Bukkit.getPlayerExact(event.getLine(1));
            if (player == null) // cannot change the bed locations of offline players
                continue;

            player.setBedSpawnLocation(bed.getLocation());
            break; // stop after the first valid sign placement
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Craft c = CraftManager.getInstance().getCraftByPlayer(p);
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
