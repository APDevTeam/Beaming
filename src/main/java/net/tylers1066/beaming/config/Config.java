package net.tylers1066.beaming.config;

import org.bukkit.inventory.ItemStack;

public class Config {
    public static String Locale = "en";

    public static boolean EnableRespawn = false;

    public static boolean EnableRespawnStrength = false;
    public static int StrengthDuration = 600;
    public static int StrengthAmplitude = 1;
    public static boolean EnableRespawnSpeed = false;
    public static int SpeedDuration = 600;
    public static int SpeedAmplitude = 1;
    public static boolean EnableRespawnResistance = false;
    public static int ResistanceDuration = 600;
    public static int ResistanceAmplitude = 5;

    public static boolean EnableRespawnMainHand = false;
    public static ItemStack RespawnMainHand = null;
    public static boolean EnableRespawnOffHand = false;
    public static ItemStack RespawnOffHand = null;

    public static boolean EnableCrewSigns = true;
    public static boolean UpdateBedLocations = false;
    public static boolean SetHomeToCrewSign = false;
}
