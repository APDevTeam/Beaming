package net.tylers1066.beaming.utils;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Utils {
    public static @Nullable String checkCrewSign(@NotNull Location signLocation, Function<Integer, String> getLineFunction) {
        if (!ChatColor.stripColor(getLineFunction.apply(0)).equalsIgnoreCase("Crew:"))
            return null;

        if (!Tag.BEDS.getValues().contains(signLocation.getBlock().getRelative(BlockFace.DOWN).getType()))
            return null;

        return getLineFunction.apply(1);
    }

    public static @Nullable String checkCrewSign(@NotNull Location signLocation) {
        if(!(signLocation.getBlock().getState() instanceof Sign))
            return null;

        Sign sign = (Sign) signLocation.getBlock().getState();
        return checkCrewSign(signLocation, sign::getLine);
    }

    @Nullable
    public static Location getCrewSign(@NotNull Craft c) {
        for (MovecraftLocation l : c.getHitBox()) {
            Location loc = l.toBukkit(c.getWorld());
            if (checkCrewSign(loc) != null)
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
