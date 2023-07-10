package mgg.code.repository;


import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.Circunscripcion;


import java.util.List;

public class CircunscripcionRepository implements CrudRepository<Circunscripcion, String> {
    private HibernateControllerCongreso hc;
    private HibernateControllerSenado hs ;


    public CircunscripcionRepository(){
        hc = HibernateControllerCongreso.getInstance();
        hs = HibernateControllerSenado.getInstance();
    }
    public List<Circunscripcion> findAll() {
        hc.open();
        TypedQuery<Circunscripcion> query = hc.getManager().createNamedQuery("Circunscripcion.findAll", Circunscripcion.class);
        List<Circunscripcion> Circunscripciones = query.getResultList();
        hc.close();
        return Circunscripciones;
    }

    public List<Circunscripcion> findAllSenado() {
        hs.open();
        TypedQuery<Circunscripcion> query = hs.getManager().createNamedQuery("Circunscripcion.findAll", Circunscripcion.class);
        List<Circunscripcion> Circunscripciones = query.getResultList();
        hs.close();
        return Circunscripciones;
    }

    public Circunscripcion getById(String id) {
        System.out.println("1");
        hc.open();
        System.out.println("Buscando Circunscripcion: ");
        Circunscripcion Circunscripcion = hc.getManager().find(Circunscripcion.class, id);
        System.out.println(Circunscripcion);
        hc.close();
        return Circunscripcion;
    }

    public Circunscripcion getByIdSenado(String id) {
        hs.open();
        Circunscripcion Circunscripcion = hs.getManager().find(Circunscripcion.class, id);
        hs.close();
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
