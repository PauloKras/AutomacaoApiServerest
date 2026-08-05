package com.serverest.login.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigurationManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties não encontrado no classpath");
            }

            properties.load(input);

        } catch (IOException ex) {
            throw new RuntimeException("Erro ao carregar config.properties", ex);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}