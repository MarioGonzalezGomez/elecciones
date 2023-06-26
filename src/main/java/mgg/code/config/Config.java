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

    public String getIpDbPrincipal() {
        return config.getProperty("ipPrincipal");
    }
    public String getIpDbReserva() {
        return config.getProperty("ipReserva");
    }

    public  String getBdCartones() {
        return config.getProperty("BDCartones");
    }
    public  String getBdFaldones() {return config.getProperty("BDFaldones");}
    public  String getipIPF() {return config.getProperty("ipIPF");}
    public  String getpuertoIPF() {return config.getProperty("puertoIPF");}

    public  String getRutaColores() {return config.getProperty("rutaColores");}
    public  String getRutaFicheros() {return config.getProperty("rutaFicheros");}


}
