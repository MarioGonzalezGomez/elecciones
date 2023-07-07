package mgg.code.repository;


import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.CP;
import mgg.code.model.Key;


import java.util.List;

public class CPRepository implements CPCrudRepository<CP, Key> {
    private HibernateControllerCongreso hc = HibernateControllerCongreso.getInstance();
    private HibernateControllerSenado hs = HibernateControllerSenado.getInstance();

    public List<CP> findAll() {
        hc.open();
        TypedQuery<CP> query = hc.getManager().createNamedQuery("CP.findAll", CP.class);
        List<CP> CPs = query.getResultList();
        hc.close();
        return CPs;
    }

    public List<CP> findAllSenado() {
        hs.open();
        TypedQuery<CP> query = hs.getManager().createNamedQuery("CP.findAll", CP.class);
        List<CP> CPs = query.getResultList();
        hs.close();
        return CPs;
    }

    public CP getById(Key key) {
        hc.open();
        CP cp = hc.getManager().find(CP.class, key);
        hc.close();
        return cp;
    }

    public CP getByIdSenado(Key key) {
        hs.open();
        CP cp = hs.getManager().find(CP.class, key);
        hs.close();
        return cp;
    }

    public CP save(CP cp) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(cp);
        hc.getTransaction().commit();
        hc.close();
        return cp;
    }

    public CP update(CP cp) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(cp);
        hc.getTransaction().commit();
        hc.close();
        return cp;
    }

    public CP delete(Key key) {
        hc.open();
        hc.getTransaction().begin();
        CP cp = hc.getManager().find(CP.class, key);
        hc.getManager().remove(cp);
        hc.getTransaction().commit();
        hc.close();
        return cp;
    }
}
