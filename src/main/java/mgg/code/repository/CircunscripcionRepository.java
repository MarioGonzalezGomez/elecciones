package mgg.code.repository;


import jakarta.persistence.TypedQuery;
import mgg.code.controller.HibernateController;
import mgg.code.model.Circunscripcion;


import java.util.List;

public class CircunscripcionRepository implements CrudRepository<Circunscripcion, String> {
    private HibernateController hc = HibernateController.getInstance();

    public List<Circunscripcion> findAll() {
        hc.open();
        TypedQuery<Circunscripcion> query = hc.getManager().createNamedQuery("Circunscripcion.findAll", Circunscripcion.class);
        List<Circunscripcion> Circunscripciones = query.getResultList();
        hc.close();
        return Circunscripciones;
    }

    public Circunscripcion getById(String id) {
        hc.open();
        Circunscripcion Circunscripcion = hc.getManager().find(Circunscripcion.class, id);
        hc.close();
        return Circunscripcion;
    }

    public Circunscripcion save(Circunscripcion Circunscripcion) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(Circunscripcion);
        hc.getTransaction().commit();
        hc.close();
        return Circunscripcion;
    }

    public Circunscripcion update(Circunscripcion Circunscripcion) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(Circunscripcion);
        hc.getTransaction().commit();
        hc.close();
        return Circunscripcion;
    }

    public Circunscripcion delete(String id) {
        hc.open();
        hc.getTransaction().begin();
        Circunscripcion Circunscripcion = hc.getManager().find(Circunscripcion.class, id);
        hc.getManager().remove(Circunscripcion);
        hc.getTransaction().commit();
        hc.close();
        return Circunscripcion;
    }
}
