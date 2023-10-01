package net.unestia.playerservice.translation;

import net.unestia.playerservice.Terminal;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class TranslationManager {

    private final List<String> enabledTranslations = new ArrayList<>();
    private final Map<String, URL> languageURL = new HashMap<>();


    public TranslationManager() {
        this.enabledTranslations.add("de_DE");
        this.enabledTranslations.add("en_US");

        this.enabledTranslations.forEach((translationKey) -> {

            this.languageURL.put(translationKey, getClass().getClassLoader().getResource(translationKey + ".properties"));

            Terminal.info("loading Language " + translationKey + "");
        });

    }

    public String getTranslationKey(String languageKey, String key) {
        Properties properties = new Properties();

        try (InputStream inputStream = this.languageURL.get(languageKey).openStream()){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (properties.getProperty(key) == null) return null;

        return properties.getProperty(key).replace("&", "§");
    }

    /*public String getTranslationKey(String languageKey, String key) {
        Properties properties = new Properties();

        try {
            properties.load(this.languageURL.get(languageKey).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (properties.getProperty(key) == null) return null;

        String returnKey = properties.getProperty(key).replace("&", "§");
        return new String(returnKey.getBytes(StandardCharsets.UTF_8));
    }*/

}
