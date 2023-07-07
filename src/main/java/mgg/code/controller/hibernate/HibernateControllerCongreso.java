package mgg.code.controller.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;

@Data
public class HibernateControllerCongreso {
    private static HibernateControllerCongreso hc;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private EntityTransaction transaction;

    private HibernateControllerCongreso() {
    }

    public static HibernateControllerCongreso getInstance() {
        if (hc == null) {
            hc = new HibernateControllerCongreso();
        }
        return hc;
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("congreso");
        manager = emf.createEntityManager();
        transaction = manager.getTransaction();
    }

    public void close() {
        manager.close();
        emf.close();
    }

}
