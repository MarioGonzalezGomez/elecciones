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
        //hc.open();
        if (!hc.getManager().isOpen())
            hc = HibernateControllerCongreso.getInstance();
        TypedQuery<CP> query = hc.getManager().createNamedQuery("CP.findAll", CP.class);
        //hc.close();
        return query.getResultList();
    }

    public List<CP> findAllSenado() {
        //hs.open();
        if (!hs.getManager().isOpen())
            hs = HibernateControllerSenado.getInstance();
        TypedQuery<CP> query = hs.getManager().createNamedQuery("CP.findAll", CP.class);
        //hs.close();
        return query.getResultList();
    }

    public CP getById(Key key) {
        if (!hc.getManager().isOpen())
            hc = HibernateControllerCongreso.getInstance();
        //hc.open();
        //hc.close();
        return hc.getManager().find(CP.class, key);
    }

    public CP getByIdSenado(Key key) {
        // hs.open();
        // hs.close();
        if (!hs.getManager().isOpen())
            hs = HibernateControllerSenado.getInstance();
        return hs.getManager().find(CP.class, key);
    }

    public CP save(CP cp) {
        if (!hc.getManager().isOpen())
            hc = HibernateControllerCongreso.getInstance();
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(cp);
        hc.getTransaction().commit();
        //hc.close();
        return cp;
    }

    public CP update(CP cp) {
        if (!hc.getManager().isOpen())
            hc = HibernateControllerCongreso.getInstance();
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(cp);
        hc.getTransaction().commit();
        //hc.close();
        return cp;
    }

    public CP delete(Key key) {
        if (!hc.getManager().isOpen())
            hc = HibernateControllerCongreso.getInstance();
        //hc.open();
        hc.getTransaction().begin();
        CP cp = hc.getManager().find(CP.class, key);
        hc.getManager().remove(cp);
        hc.getTransaction().commit();
        //hc.close();
        return cp;
    }
}
