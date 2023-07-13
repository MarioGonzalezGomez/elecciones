package mgg.code.repository;

import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.Partido;


import java.util.List;


public class PartidoRepository implements CrudRepository<Partido, String> {
    private HibernateControllerCongreso hc = HibernateControllerCongreso.getInstance();
    private HibernateControllerSenado hs = HibernateControllerSenado.getInstance();

    public List<Partido> findAll() {
        //hc.open();
        TypedQuery<Partido> query = hc.getManager().createNamedQuery("Partido.findAll", Partido.class);
        //hc.close();
        return query.getResultList();
    }

    public List<Partido> findAllSenado() {
        //hs.open();
        TypedQuery<Partido> query = hs.getManager().createNamedQuery("Partido.findAll", Partido.class);
        //hs.close();
        return query.getResultList();
    }

    public Partido getById(String id) {
        //hc.open();
        //hc.close();
        return hc.getManager().find(Partido.class, id);
    }


    public Partido getByIdSenado(String id) {
        //hs.open();
        //hs.close();
        return hs.getManager().find(Partido.class, id);
    }

    public Partido save(Partido partido) {
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(partido);
        hc.getTransaction().commit();
        //hc.close();
        return partido;
    }

    public Partido update(Partido partido) {
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(partido);
        hc.getTransaction().commit();
        //hc.close();
        return partido;
    }

    public Partido delete(String id) {
        //hc.open();
        hc.getTransaction().begin();
        Partido partido = hc.getManager().find(Partido.class, id);
        hc.getManager().remove(partido);
        hc.getTransaction().commit();
        //hc.close();
        return partido;
    }

}
