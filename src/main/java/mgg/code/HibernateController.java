package mgg.code;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.Data;

@Data
public class HibernateController {
    private static HibernateController hc;
    private EntityManagerFactory emf;
    private EntityManager manager;
    private EntityTransaction transaction;

    private HibernateController() {
    }

    public static HibernateController getInstance() {
        if (hc == null) {
            hc = new HibernateController();
        }
        return hc;
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("elecciones");
        manager = emf.createEntityManager();
        transaction = manager.getTransaction();
    }

    public void close() {
        manager.close();
        emf.close();
    }

}
