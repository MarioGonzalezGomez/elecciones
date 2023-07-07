package mgg.code.controller.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;

@Data
public class HibernateControllerSenado {
    private static HibernateControllerSenado hc;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private EntityTransaction transaction;

    private HibernateControllerSenado() {
    }

    public static HibernateControllerSenado getInstance() {
        if (hc == null) {
            hc = new HibernateControllerSenado();
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
