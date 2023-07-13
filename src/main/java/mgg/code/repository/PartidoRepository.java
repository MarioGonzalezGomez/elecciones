package mgg.code.repository;

import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.Partido;
import mgg.code.util.Timer;


import java.util.List;


public class PartidoRepository implements CrudRepository<Partido, String> {
    private HibernateControllerCongreso hc = HibernateControllerCongreso.getInstance();
    private HibernateControllerSenado hs = HibernateControllerSenado.getInstance();
    Timer timer = Timer.getInstance();

    public List<Partido> findAll() {
        //hc.open();
        timer.startTimer("[PARTIDO]");
        TypedQuery<Partido> query = hc.getManager().createNamedQuery("Partido.findAll", Partido.class);
        List<Partido> partidos = query.getResultList();
        timer.calculateTime("[PARTIDO]");

        //hc.close();
        return partidos;
    }

    public List<Partido> findAllSenado() {
        hs.open();
        timer.startTimer("[PARTIDO]");

        TypedQuery<Partido> query = hs.getManager().createNamedQuery("Partido.findAll", Partido.class);
        List<Partido> partidos = query.getResultList();
        timer.calculateTime("[PARTIDO]");

        hs.close();
        return partidos;
    }

    public Partido getById(String id) {
        //hc.open();
        timer.startTimer("[PARTIDO]");

        System.out.println("Buscando partido...");
        Partido partido = hc.getManager().find(Partido.class, id);
        System.out.println(partido);
        timer.calculateTime("[PARTIDO]");

        //hc.close();
        return partido;
    }


    public Partido getByIdSenado(String id) {
        hs.open();
        timer.startTimer("[PARTIDO]");

        Partido partido = hs.getManager().find(Partido.class, id);
        timer.calculateTime("[PARTIDO]");

        hs.close();
        return partido;
    }

    public Partido save(Partido partido) {
        //hc.open();
        timer.startTimer("[PARTIDO]");

        hc.getTransaction().begin();
        hc.getManager().persist(partido);
        hc.getTransaction().commit();
        timer.calculateTime("[PARTIDO]");

        //hc.close();
        return partido;
    }

    public Partido update(Partido partido) {
        //hc.open();
        timer.startTimer("[PARTIDO]");

        hc.getTransaction().begin();
        hc.getManager().merge(partido);
        hc.getTransaction().commit();
        timer.calculateTime("[PARTIDO]");

        //hc.close();
        return partido;
    }

    public Partido delete(String id) {
        //hc.open();
        timer.startTimer("[PARTIDO]");

        hc.getTransaction().begin();
        Partido partido = hc.getManager().find(Partido.class, id);
        hc.getManager().remove(partido);
        hc.getTransaction().commit();
        timer.calculateTime("[PARTIDO]");

        //hc.close();
        return partido;
    }

}
