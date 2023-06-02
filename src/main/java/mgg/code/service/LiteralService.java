package mgg.code.service;


import mgg.code.model.Literal;
import mgg.code.repository.LiteralRepository;

import java.util.List;

public class LiteralService extends BaseService<Literal, Integer, LiteralRepository> {

    public LiteralService(LiteralRepository repository) {
        super(repository);
    }

    public List<Literal> getAllLiterals() {
        return this.findAll();
    }

    public Literal getLiteralById(int id) {
        return this.getById(id);
    }

    public Literal postLiteral(Literal literal) {
        return this.save(literal);
    }

    public Literal updateLiteral(Literal literal) {
        return this.update(literal);
    }

    public Literal deleteLiteral(Literal literal) {
        return this.delete(literal.getId());
    }
}
