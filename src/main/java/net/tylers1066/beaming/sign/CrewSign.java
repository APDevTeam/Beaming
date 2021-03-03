package net.tylers1066.beaming.sign;

import com.earth2me.essentials.User;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;
import net.tylers1066.beaming.localisation.I18nSupport;
import net.tylers1066.beaming.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import org.jetbrains.annotations.Nullable;

public class CrewSign implements Listener {
    @EventHandler
    public final void onSignChange(SignChangeEvent event) {
        if (!event.getLine(0).equalsIgnoreCase("Crew:")) {
            return;
        }
        Player player = event.getPlayer();
        event.setLine(1, player.getName());
    }

    @EventHandler
    public final void onSignRightClick(PlayerInteractEvent event) {
        if (!Config.EnableCrewSigns || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking() || !(event.getClickedBlock().getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) event.getClickedBlock().getState();
        if (!sign.getLine(0).equalsIgnoreCase("Crew:")) {
            return;
        }
        if (!Utils.isBed(sign.getBlock().getRelative(0,-1,0).getType())) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Need Bed Below"));
            return;
        }
        if (!sign.getLine(1).equalsIgnoreCase(player.getName())) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Sign Not Owned"));
            return;
        }
        if(CraftManager.getInstance().getCraftByPlayer(player) != null) {
            player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Craft Currently Piloted"));
            return;
        }
        Location location = sign.getLocation();
        player.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Spawn Set"));
        player.setBedSpawnLocation(location, true);

        if (!Config.SetHomeToCrewSign || Beaming.getInstance().getEssentials() == null) {
            return;
        }
        User u = Beaming.getInstance().getEssentials().getUser(player);
        u.setHome("home", location);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Craft c = CraftManager.getInstance().getCraftByPlayer(p);
        if(c == null)
            return;
        if(c.getSinking() || c.getDisabled())
            return;

        Location sign = getCrewSign(c);
        if(sign == null)
            return;

        Location respawn = getRespawnLocation(sign);
        if(respawn == null)
            return;

        e.setRespawnLocation(respawn);
        p.sendMessage(I18nSupport.getInternationalisedString("CrewSign - Respawn"));
    }

    @Nullable
    private Location getCrewSign(Craft c) {
        World w = c.getW();
        for(MovecraftLocation l : c.getHitBox()) {
            if(!Utils.isSign(l.toBukkit(c.getW()).getBlock().getType()))
                continue;

            Location loc = l.toBukkit(c.getW());
            Sign sign = (Sign) loc.getBlock().getState();
            if(!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("Crew:"))
                continue;

            if(!Utils.isBed(l.translate(0, -1, 0).toBukkit(w).getBlock().getType()))
                continue;

            return loc;
        }
        return null;
    }

    @Nullable
    private Location getRespawnLocation(Location sign) {
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Location l = new Location(sign.getWorld(), sign.getBlockX()+i, sign.getBlockY()-1, sign.getBlockZ()+j);
                if(!l.getBlock().getType().isAir())
                    continue;

                if(!l.getBlock().getRelative(BlockFace.UP).isEmpty())
                    continue;

                if(l.getBlock().getRelative(BlockFace.DOWN).isEmpty())
                    continue;

                return l;
            }
        }
        return null;
    }
}
