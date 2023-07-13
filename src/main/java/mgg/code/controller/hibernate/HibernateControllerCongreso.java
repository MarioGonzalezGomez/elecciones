package mgg.code.controller.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.Map;

@Data
public class HibernateControllerCongreso {
    private static HibernateControllerCongreso hc;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private EntityTransaction transaction;

    private HibernateControllerCongreso() {
    }

    public static void cambioReserva() {
        String reservaUrl = "jdbc:mysql://172.28.51.22:3306,172.28.51.21:3306,127.0.0.1:3306/elecciones_generales_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

        hc = new HibernateControllerCongreso();
        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        hc.manager = hc.emf.createEntityManager();
        hc.transaction = hc.manager.getTransaction();
        System.out.println("Cambiando a reserva...");
        System.out.println("Base de datos cambiada a reserva");
        System.out.println(hc.getEmf().unwrap(SessionFactory.class).getProperties().get("hibernate.connection.url"));

    }

    public static void cambioPrincipal() {
        String reservaUrl = "jdbc:mysql://172.28.51.21:3306,172.28.51.22:3306,127.0.0.1:3306/elecciones_generales_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

        hc = new HibernateControllerCongreso();

        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        hc.manager = hc.emf.createEntityManager();
        hc.transaction = hc.manager.getTransaction();
        System.out.println("Base de datos cambiada a principal");

    }

    public static void cambioLocal() {
        String reservaUrl = "jdbc:mysql://127.0.0.1:3306,172.28.51.21:3306,172.28.51.22:3306/elecciones_generales_2023";
        String reservaUser = "root";
        String reservaPass = "auto1041";

        if (hc != null) {
            hc.close();
        }

        hc = new HibernateControllerCongreso();


        hc.setEmf(Persistence.createEntityManagerFactory("congreso", getProperties(reservaUrl, reservaUser, reservaPass)));
        hc.manager = hc.emf.createEntityManager();
        hc.transaction = hc.manager.getTransaction();
        System.out.println("Base de datos cambiada a local");

    }

    private static Map<String, String> getProperties(String url, String username, String password) {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", url);
        properties.put("javax.persistence.jdbc.user", username);
        properties.put("javax.persistence.jdbc.password", password);
        return properties;
    }

    public static HibernateControllerCongreso getInstance() {
        if (hc == null) {
            hc = new HibernateControllerCongreso();
            hc.open();
        }
        return hc;
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("congreso");
        manager = emf.createEntityManager();
        transaction = manager.getTransaction();
        System.out.println(hc.getManager().getProperties().keySet());
        System.out.println(emf.unwrap(SessionFactory.class).getProperties().get("hibernate.connection.url"));
    }

    public void close() {
        manager.close();
        emf.close();
    }


}
