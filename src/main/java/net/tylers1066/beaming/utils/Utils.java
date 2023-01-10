package net.tylers1066.beaming.utils;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Utils {
    public static boolean isCrewSign(@NotNull Function<Integer, String> getLineFunction) {
        return ChatColor.stripColor(getLineFunction.apply(0)).equalsIgnoreCase("Crew:");
    }

    public static @NotNull Block getBedBlock(@NotNull Location signLocation) {
        return signLocation.getBlock().getRelative(BlockFace.DOWN);
    }

    public static boolean isBed(@NotNull Block block) {
        return Tag.BEDS.getValues().contains(block.getType());
    }

    public static @Nullable String checkCrewSign(@NotNull Location signLocation, @NotNull Function<Integer, String> getLineFunction) {
        if (!isCrewSign(getLineFunction))
            return null;

        if (!isBed(getBedBlock(signLocation)))
            return null;

        return getLineFunction.apply(1);
    }

    public static @Nullable String checkCrewSign(@NotNull Location signLocation) {
        if(!(signLocation.getBlock().getState() instanceof Sign))
            return null;

        Sign sign = (Sign) signLocation.getBlock().getState();
        return checkCrewSign(signLocation, sign::getLine);
    }

    public static @NotNull Map<String, Location> getAllCrewSigns(@NotNull Craft craft) {
        Map<String, Location> signs = new HashMap<>();
        for (MovecraftLocation l : craft.getHitBox()) {
            Location location = l.toBukkit(craft.getWorld());
            String name = checkCrewSign(location);
            if (name != null)
                signs.putIfAbsent(name, location);
        }
        return signs;
    }

    public static @Nullable Location getCrewSign(@NotNull Craft craft, @NotNull String name) {
        for (MovecraftLocation l : craft.getHitBox()) {
            Location location = l.toBukkit(craft.getWorld());
            if (name.equalsIgnoreCase(checkCrewSign(location)))
                return location;
        }
        return null;
    }

    /**
     * @deprecated Crew signs have a specific player
     * @see #getCrewSign(Craft, String)
     */
    @Deprecated
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
