package mgg.code.repository;


import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.CP;
import mgg.code.model.Key;
import mgg.code.util.Timer;


import java.util.List;

public class CPRepository implements CPCrudRepository<CP, Key> {
    private HibernateControllerCongreso hc = HibernateControllerCongreso.getInstance();
    private HibernateControllerSenado hs = HibernateControllerSenado.getInstance();

    Timer timer = Timer.getInstance();

    public List<CP> findAll() {
        //hc.open();
        timer.startTimer("[CP]");
        TypedQuery<CP> query = hc.getManager().createNamedQuery("CP.findAll", CP.class);
        List<CP> CPs = query.getResultList();
        timer.calculateTime("[CP]");
        //hc.close();
        return CPs;
    }

    public List<CP> findAllSenado() {
        hs.open();
        timer.startTimer("[CP]");

        TypedQuery<CP> query = hs.getManager().createNamedQuery("CP.findAll", CP.class);
        List<CP> CPs = query.getResultList();
        timer.calculateTime("[CP]");
        hs.close();
        return CPs;
    }

    public CP getById(Key key) {
        //hc.open();
        timer.startTimer("[CP]");

        System.out.println("Buscando CP: ");
        CP cp = hc.getManager().find(CP.class, key);
        System.out.println(cp);
        timer.calculateTime("[CP]");
        //hc.close();
        return cp;
    }

    public CP getByIdSenado(Key key) {
        hs.open();
        timer.startTimer("[CP]");

        CP cp = hs.getManager().find(CP.class, key);
        timer.calculateTime("[CP]");
        hs.close();
        return cp;
    }

    public CP save(CP cp) {
        //hc.open();
        timer.startTimer("[CP]");

        hc.getTransaction().begin();
        hc.getManager().persist(cp);
        hc.getTransaction().commit();
        timer.calculateTime("[CP]");
        //hc.close();
        return cp;
    }

    public CP update(CP cp) {
        //hc.open();
        timer.startTimer("[CP]");

        hc.getTransaction().begin();
        hc.getManager().merge(cp);
        hc.getTransaction().commit();
        timer.calculateTime("[CP]");
        //hc.close();
        return cp;
    }

    public CP delete(Key key) {
        //hc.open();
        timer.startTimer("[CP]");

        hc.getTransaction().begin();
        CP cp = hc.getManager().find(CP.class, key);
        hc.getManager().remove(cp);
        hc.getTransaction().commit();
        timer.calculateTime("[CP]");
        //hc.close();
        return cp;
    }
}
