package net.tylers1066.beaming.utils;

import org.bukkit.Material;

public class Utils {
    public static boolean isBed(Material m) {
        return m.name().endsWith("_BED");
    }

    public static boolean isSign(Material m) {
        return m.name().endsWith("_SIGN") || m.name().equals("SIGN_POST");
    }
}
