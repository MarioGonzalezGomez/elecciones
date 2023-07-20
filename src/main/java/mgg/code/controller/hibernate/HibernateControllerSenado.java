package mgg.code.controller.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HibernateControllerSenado {
    private static HibernateControllerSenado hc;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private EntityTransaction transaction;

    private HibernateControllerSenado() {
    }

    public static void cambioReserva(){
        String reservaUrl = "jdbc:mysql://172.28.51.22:3306,172.28.51.21:3306,127.0.0.1:3306/elecciones_senado_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

        if (hc != null) {
            hc.open();
        }

        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        System.out.println("Base de datos de senado cambiada a reserva");
    }

    public static void cambioPrincipal(){
        String reservaUrl = "jdbc:mysql://172.28.51.21:3306,172.28.51.22:3306,127.0.0.1:3306/elecciones_senado_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

        //hc = new HibernateControllerSenado();
        if (hc != null) {
            hc.open();
        }

        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        System.out.println("Base de datos de senado cambiada a principal");

    }

    public static void cambioLocal(){
        String reservaUrl = "jdbc:mysql://127.0.0.1:3306,172.28.51.21:3306,172.28.51.22:3306/elecciones_senado_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

       // hc = new HibernateControllerSenado();
        if (hc != null) {
            hc.open();
        }

        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        System.out.println("Base de datos de senado cambiada a local");

    }

    private static Map<String, String> getProperties(String url, String username, String password) {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", username);
        properties.put("javax.persistence.jdbc.password", password);
        return properties;
    }

    public static HibernateControllerSenado getInstance() {
        if (hc == null) {
            hc = new HibernateControllerSenado();
            hc.open();
        }
        return hc;
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("senado");
        manager = emf.createEntityManager();
        transaction = manager.getTransaction();
    }

    public void close() {
        manager.close();
        emf.close();
    }

}
