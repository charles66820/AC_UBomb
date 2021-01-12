package fr.ubx.poo.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class LangFactory {

    // Store lang with key is theme and code
    private final Map<String, Map<String, String>> langs = new HashMap<>();

    private Map<String, String> currentLang;

    private LangFactory() {
    }

    /**
     * Access point for the single instance of the singleton
     */
    public static LangFactory getInstance() {
        return LangFactory.Holder.instance;
    }

    public static String get(String key) {
        return LangFactory.Holder.instance.getValue(key);
    }

    public void setLang(String theme, String code) {
        if (langs.get(theme + code) == null) loadLang(theme, code);
        else this.currentLang = langs.get(theme + code);
    }

    private void loadLang(String theme, String code) {
        // Add lang
        Map<String, String> lang = new HashMap<>();
        langs.put(theme + code, lang);
        this.currentLang = lang;

        // Load lang properties
        String filePath = "/themes/" + theme + "/lang/" + code + ".properties";
        try (InputStream input = getClass().getResourceAsStream(filePath)) {
            if (input == null) throw new IOException(filePath + "(No such file or directory)");

            Properties prop = new Properties();
            prop.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            Enumeration<Object> enu = prop.keys();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                lang.put(key, prop.getProperty(key, "undefined"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        String value = this.currentLang.get(key);
        return value != null ? value : "undefined";
    }

    /**
     * Holder
     */
    private static class Holder {
        /**
         * Single instance not pre-initialized
         */
        private final static LangFactory instance = new LangFactory();
    }

}
