package net.tylers1066.beaming.sign;

import com.earth2me.essentials.User;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.CraftPilotEvent;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import net.countercraft.movecraft.events.CraftSinkEvent;
import net.countercraft.movecraft.events.ManOverboardEvent;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;
import net.tylers1066.beaming.localisation.I18nSupport;
import net.tylers1066.beaming.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
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
import java.util.Map;

public class CrewSign implements Listener {

    private final Map<String, Craft> respawnCrafts = new HashMap<>();

    private void removeRespawnCraft(@NotNull Craft craft) {
        respawnCrafts.values().removeIf(c -> c == craft);
    }

    private void setRespawnCraft(@NotNull String name, @NotNull Craft craft) {
        respawnCrafts.put(name, craft);
    }

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
    public void onPilot(@NotNull CraftPilotEvent event) {
        for (Map.Entry<String, Location> entry : Utils.getAllCrewSigns(event.getCraft()).entrySet()) {
            setRespawnCraft(entry.getKey(), event.getCraft());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSink(CraftSinkEvent event) {
        // do not allow respawning on sinking crafts
        removeRespawnCraft(event.getCraft());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onRelease(@NotNull CraftReleaseEvent event) {
        removeRespawnCraft(event.getCraft());

        if (!Config.UpdateBedLocations)
            return;

        for (Map.Entry<String, Location> entry : Utils.getAllCrewSigns(event.getCraft()).entrySet()) {

            Player player = Bukkit.getPlayerExact(entry.getKey());
            if (player == null) // cannot change the bed locations of offline players
                continue;

            Block bed = Utils.getBedBlock(entry.getValue());
            player.setBedSpawnLocation(bed.getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent e) {
        if (!Config.HandleRespawnWhenPiloting)
            return;

        String name = e.getPlayer().getName();
        Craft craft = respawnCrafts.get(name);
        if (craft == null)
            return;
        if (craft instanceof SinkingCraft || craft.getDisabled()) {
            // do not allow respawning on sinking or disabled crafts
            removeRespawnCraft(craft);
            return;
        }

        Location sign = Utils.getCrewSign(craft, name);
        if (sign == null) { // crew sign was destroyed
            respawnCrafts.remove(name);
            return;
        }

        Location respawn = Utils.getRespawnLocation(sign);
        if (respawn == null)
            return;

        e.setRespawnLocation(respawn);
        e.getPlayer().sendMessage(I18nSupport.getInternationalisedString("CrewSign - Respawn"));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onManOverboard(@NotNull ManOverboardEvent e) {
        if (!Config.HandleManOverBoard || !(e.getCraft() instanceof PlayerCraft))
            return;

        Player pilot = ((PlayerCraft) e.getCraft()).getPilot();
        Location sign = Utils.getCrewSign(e.getCraft(), pilot.getName());
        if (sign == null)
            return;

        Location respawn = Utils.getRespawnLocation(sign);
        if (respawn == null)
            return;

        e.setLocation(respawn);
    }
}
