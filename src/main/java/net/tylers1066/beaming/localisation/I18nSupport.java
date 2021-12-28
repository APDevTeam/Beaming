package net.tylers1066.beaming.localisation;

import net.tylers1066.beaming.Beaming;
import net.tylers1066.beaming.config.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class I18nSupport {
    private static Properties languageFile;

    public static void init() {
        languageFile = new Properties();

        File localisationDirectory = new File(Beaming.getInstance().getDataFolder().getAbsolutePath() + "/localisation");

        if (!localisationDirectory.exists()) {
            localisationDirectory.mkdirs();
        }

        InputStream is = null;
        try {
            is = new FileInputStream(localisationDirectory.getAbsolutePath() + "/beaminglang" + "_" + Config.Locale + ".properties");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (is == null) {
            Beaming.getInstance().getLogger().log(Level.SEVERE, "Critical Error in Localisation System");
            Beaming.getInstance().getServer().shutdown();
            return;
        }

        try {
            languageFile.load(is);
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String getInternationalisedString(String key) {
        String ret = languageFile.getProperty(key);
        if (ret != null)
            return ret;
        else
            return key;
    }
}
