package net.tylers1066.beaming.utils;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
    @Nullable
    public static Location getCrewSign(@NotNull Craft c) {
        World w = c.getWorld();
        for (MovecraftLocation l : c.getHitBox()) {
            Location loc = l.toBukkit(c.getWorld());
            if(!(loc.getBlock().getState() instanceof Sign))
                continue;

            Sign sign = (Sign) loc.getBlock().getState();
            if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("Crew:"))
                continue;

            if (!Tag.BEDS.getValues().contains(l.translate(0, -1, 0).toBukkit(w).getBlock().getType()))
                continue;

            return loc;
        }
        return null;
    }

    @Nullable
    public static Location getRespawnLocation(Location sign) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Location l = new Location(sign.getWorld(), sign.getBlockX() + i, sign.getBlockY() - 1, sign.getBlockZ() + j);
                if (!l.getBlock().isEmpty())
                    continue;

                if (!l.getBlock().getRelative(BlockFace.UP).isEmpty())
                    continue;

                if (l.getBlock().getRelative(BlockFace.DOWN).isEmpty())
                    continue;

                return l;
            }
        }
        return null;
    }
}
