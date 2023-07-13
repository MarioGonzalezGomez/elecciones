package mgg.code.repository;


import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.Circunscripcion;
import mgg.code.util.Timer;


import java.util.List;

public class CircunscripcionRepository implements CrudRepository<Circunscripcion, String> {
    private HibernateControllerCongreso hc;
    private HibernateControllerSenado hs;
    private Timer timer;


    public CircunscripcionRepository() {
        hc = HibernateControllerCongreso.getInstance();
        hs = HibernateControllerSenado.getInstance();
        timer = Timer.getInstance();
    }

    public List<Circunscripcion> findAll() {
        //hc.open();
        TypedQuery<Circunscripcion> query = hc.getManager().createNamedQuery("Circunscripcion.findAll", Circunscripcion.class);
        //hc.close();
        return query.getResultList();
    }

    public List<Circunscripcion> findAllSenado() {
        //hs.open();
        TypedQuery<Circunscripcion> query = hs.getManager().createNamedQuery("Circunscripcion.findAll", Circunscripcion.class);
        //hs.close();
        return query.getResultList();
    }

    public Circunscripcion getById(String id) {
        //hc.open();
        //hc.close();
        return hc.getManager().find(Circunscripcion.class, id);
    }

    public Circunscripcion getByIdSenado(String id) {
        //hs.open();
        //hs.close();
        return hs.getManager().find(Circunscripcion.class, id);
    }

    public Circunscripcion save(Circunscripcion Circunscripcion) {
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(Circunscripcion);
        hc.getTransaction().commit();
        //hc.close();
        return Circunscripcion;
    }

    public Circunscripcion update(Circunscripcion Circunscripcion) {
        //hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(Circunscripcion);
        hc.getTransaction().commit();
        //hc.close();
        return Circunscripcion;
    }

    public Circunscripcion delete(String id) {
        //hc.open();
        hc.getTransaction().begin();
        Circunscripcion Circunscripcion = hc.getManager().find(Circunscripcion.class, id);
        hc.getManager().remove(Circunscripcion);
        hc.getTransaction().commit();
        //hc.close();
        return Circunscripcion;
    }
}
