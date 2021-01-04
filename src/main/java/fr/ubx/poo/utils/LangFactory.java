package fr.ubx.poo.utils;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class LangFactory {

    private final Map<String, Map<String, String>> langs = new HashMap<>();

    private Map<String, String> currentLang;

    private LangFactory() {
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static LangFactory getInstance() {
        return LangFactory.Holder.instance;
    }

    public static String get(String key) {
        return LangFactory.Holder.instance.getValue(key);
    }

    public void setLang(String theme, String code) {
        if (langs.get(code) == null) loadLang(theme, code);
        else this.currentLang = langs.get(code);
    }

    private void loadLang(String theme, String code) {
        String langPath = getClass().getResource("/themes/" + theme + "/lang/").getFile();

        // Add lang
        Map<String, String> lang = new HashMap<>();
        langs.put(code, lang);
        // Load lang properties
        try (InputStream input = new FileInputStream(new File(langPath, code + ".properties"))) {
            Properties prop = new Properties();
            prop.load(input);
            Enumeration<Object> enu = prop.keys();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                lang.put(key, prop.getProperty(key, "undefined"));
            }
            this.currentLang = lang;
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
         * Instance unique non préinitialisée
         */
        private final static LangFactory instance = new LangFactory();
    }

}
