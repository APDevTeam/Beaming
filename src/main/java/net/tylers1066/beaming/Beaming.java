package net.tylers1066.beaming;

import com.earth2me.essentials.Essentials;
import net.tylers1066.beaming.commands.BeamCommand;
import net.tylers1066.beaming.config.Config;
import net.tylers1066.beaming.listener.DeathListener;
import net.tylers1066.beaming.listener.RespawnListener;
import net.tylers1066.beaming.localisation.I18nSupport;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;

public class Beaming extends JavaPlugin implements Listener{
    public final static String PREFIX = ChatColor.DARK_BLUE + "[" + ChatColor.YELLOW + "Beaming" + ChatColor.DARK_BLUE + "] " + ChatColor.RED;
    private static Beaming instance;
    private static Essentials essentials = null;

    public static Beaming getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Config.Locale = getConfig().getString("Locale", "en");
        String[] localisations = {"en"};
        for (String s : localisations) {
            if (!new File(getDataFolder()
                    + "/localisation/beaminglang_" + s + ".properties").exists()) {
                this.saveResource("localisation/beaminglang_" + s + ".properties", false);
            }
        }
        I18nSupport.init();


        if(getConfig().getBoolean("EnableRespawn", false)) {
            try {
                Config.EnableRespawn = true;
                Config.EnableRespawnStrength = getConfig().getBoolean("EnableRespawnStrength", false);
                Config.StrengthDuration = 20 * getConfig().getInt("StrengthDuration", 30);
                Config.StrengthAmplitude = getConfig().getInt("StrengthAmplitude", 1);
                Config.EnableRespawnSpeed = getConfig().getBoolean("EnableRespawnSpeed", false);
                Config.SpeedDuration = 20 * getConfig().getInt("SpeedDuration", 30);
                Config.SpeedAmplitude = getConfig().getInt("SpeedAmplitude", 1);
                Config.EnableRespawnResistance = getConfig().getBoolean("EnableRespawnResistance", false);
                Config.ResistanceDuration = 20 * getConfig().getInt("ResistanceDuration", 30);
                Config.ResistanceAmplitude = getConfig().getInt("ResistanceAmplitude", 5);

                if (getConfig().contains("RespawnMainHand") && getConfig().getBoolean("RespawnMainHand.Use")) {
                    Config.EnableRespawnMainHand = true;
                    Config.RespawnMainHand = new ItemStack(Material.getMaterial(getConfig().getString("RespawnMainHand.Item")));

                    ItemMeta im = Config.RespawnMainHand.getItemMeta();
                    im.setDisplayName(getConfig().getString("RespawnMainHand.Name"));
                    im.setLore(getConfig().getStringList("RespawnMainHand.Lore"));
                    Config.RespawnMainHand.setItemMeta(im);

                    Map<String, Object> enchants = getConfig().getConfigurationSection("RespawnMainHand.Enchants").getValues(false);
                    for (String s : enchants.keySet()) {
                        Object o = enchants.get(s);
                        if(!(o instanceof Integer))
                            continue;

                        Config.RespawnMainHand.addUnsafeEnchantment(Enchantment.getByName(s), (Integer) enchants.get(s));
                    }
                }
                if (getConfig().contains("RespawnOffHand") && getConfig().getBoolean("RespawnOffHand.Use")) {
                    Config.EnableRespawnOffHand = true;
                    Config.RespawnOffHand = new ItemStack(Material.getMaterial(getConfig().getString("RespawnOffHand.Item")));

                    ItemMeta im = Config.RespawnOffHand.getItemMeta();
                    im.setDisplayName(getConfig().getString("RespawnOffHand.Name"));
                    im.setLore(getConfig().getStringList("RespawnOffHand.Lore"));
                    Config.RespawnOffHand.setItemMeta(im);

                    Map<String, Object> enchants = getConfig().getConfigurationSection("RespawnOffHand.Enchants").getValues(false);
                    for (String s : enchants.keySet()) {
                        Object o = enchants.get(s);
                        if(!(o instanceof Integer))
                            continue;

                        Config.RespawnOffHand.addUnsafeEnchantment(Enchantment.getByName(s), (Integer) enchants.get(s));
                    }
                }
            }
            catch (Exception e) {
                getLogger().severe("Failed to load config!");
                throw e;
            }
            getLogger().info("Loaded config.");

            getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        }

        Config.EnableCrewSigns = getConfig().getBoolean("EnableCrewSigns", true);
        Config.SetHomeToCrewSign = getConfig().getBoolean("SetHomeToCrewSign", false);

        if(Config.SetHomeToCrewSign) {
            Plugin p = getServer().getPluginManager().getPlugin("Essentials");
            if (p != null && p.getDescription().getName().equalsIgnoreCase("essentials")
                    && p.getClass().getName().equals("com.earth2me.essentials.Essentials")
                    && p instanceof Essentials) {
                essentials = (Essentials) p;
                getLogger().log(Level.INFO, I18nSupport.getInternationalisedString("Startup - Essentials Found"));
            }
            else {
                getLogger().log(Level.INFO, I18nSupport.getInternationalisedString("Startup - Essentials Not Found"));
            }
        }


        getCommand("beam").setExecutor(new BeamCommand());
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
    }

    @Override
    public void onDisable() {

    }

    @Nullable
    public Essentials getEssentials() {
        return essentials;
    }
}