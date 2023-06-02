package mgg.code.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Config configuracion;
    public static Properties config;

    //TODO:Add logica de connected server
    public static String connectedServer;


    private Config() {
        config = new Properties();
        loadConfig();
    }

    public static Config getConfiguracion() {
        if (configuracion == null) {
            configuracion = new Config();
        }
        return configuracion;
    }

    public void loadConfig() {
        try (InputStream in = getClass().getResourceAsStream("/config.properties")) {
            config.load(in);
        } catch (Exception e) {
            System.out.println("Error cargando configuración");
        }
    }

    public static String getIpDbPrincipal() {
        return config.getProperty("ipPrincipal");
    }
    public static String getIpDbReserva() {
        return config.getProperty("ipReserva");
    }

    public static String getBdCartones() {
        return config.getProperty("BDCartones");
    }
    public static String getBdFaldones() {return config.getProperty("BDFaldones");}
    public static String getipIPF() {return config.getProperty("ipIPF");}
    public static String getpuertoIPF() {return config.getProperty("puertoIPF");}

    public static String getRutaColores() {return config.getProperty("rutaColores");}
    public static String getRutaFicheros() {return config.getProperty("rutaFicheros");}


}
