package mgg.code.config;

import java.io.FileInputStream;
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
        System.out.println(configuracion);
        return configuracion;
    }

    public void loadConfig() {
        try (FileInputStream stream = new FileInputStream("C:\\Elecciones2023\\config.properties")) {
            config.load(stream);
        } catch (Exception e) {
            System.out.println("Error cargando configuración");
        }
    }

    public String getIpDbPrincipal() {
        return config.getProperty("BDPrincipal");
    }
    public String getIpDbReserva() {
        return config.getProperty("BDReserva");
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
