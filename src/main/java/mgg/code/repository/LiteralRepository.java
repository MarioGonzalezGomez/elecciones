package mgg.code.repository;

import jakarta.persistence.TypedQuery;
import mgg.code.controller.hibernate.HibernateControllerCongreso;
import mgg.code.controller.hibernate.HibernateControllerSenado;
import mgg.code.model.Literal;

import java.util.List;

public class LiteralRepository implements CrudRepository<Literal, Integer> {

    private HibernateControllerCongreso hc = HibernateControllerCongreso.getInstance();
    private HibernateControllerSenado hs = HibernateControllerSenado.getInstance();

    @Override
    public List<Literal> findAll() {
        hc.open();
        TypedQuery<Literal> query = hc.getManager().createNamedQuery("Literal.findAll", Literal.class);
        List<Literal> literales = query.getResultList();
        hc.close();
        return literales;
    }

    public List<Literal> findAllSenado() {
        hs.open();
        TypedQuery<Literal> query = hs.getManager().createNamedQuery("Literal.findAll", Literal.class);
        List<Literal> literales = query.getResultList();
        hs.close();
        return literales;
    }

    @Override
    public Literal getById(Integer id) {
        hc.open();
        Literal literal = hc.getManager().find(Literal.class, id);
        hc.close();
        return literal;
    }

    public Literal getByIdSenado(Integer id) {
        hs.open();
        Literal literal = hs.getManager().find(Literal.class, id);
        hs.close();
        return literal;
    }

    @Override
    public Literal save(Literal literal) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().persist(literal);
        hc.getTransaction().commit();
        hc.close();
        return literal;
    }

    @Override
    public Literal update(Literal literal) {
        hc.open();
        hc.getTransaction().begin();
        hc.getManager().merge(literal);
        hc.getTransaction().commit();
        hc.close();
        return literal;
    }

    @Override
    public Literal delete(Integer id) {
        hc.open();
        hc.getTransaction().begin();
        Literal literal = hc.getManager().find(Literal.class, id);
        hc.getManager().remove(literal);
        hc.getTransaction().commit();
        hc.close();
        return literal;
    }
}
