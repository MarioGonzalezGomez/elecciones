package mgg.code.controller;

import mgg.code.model.Literal;
import mgg.code.repository.LiteralRepository;
import mgg.code.service.LiteralService;

import java.util.List;

public class LiteralController {
    private static LiteralController controller = null;
    private final LiteralService service;

    private LiteralController(LiteralService service) {
        this.service = service;
    }

    public static LiteralController getInstance() {
        if (controller == null) {
            controller = new LiteralController(new LiteralService(new LiteralRepository()));
        }
        return controller;
    }

    public List<Literal> getAllLiterales() {return service.getAllLiterals();}
    public List<Literal> getAllLiteralesSenado() {return service.getAllLiteralsSenado();}
    public Literal getLiteralById(int id) {
        return service.getLiteralById(id);
    }
    public Literal getLiteralByIdSenado(int id) {return service.getLiteralByIdSenado(id);}

    public Literal postLiteral(Literal Literal) {
        return service.postLiteral(Literal);
    }

    public Literal updateLiteral(Literal Literal) {
        return service.updateLiteral(Literal);
    }

    public Literal deleteLiteral(Literal Literal) {
        return service.deleteLiteral(Literal);
    }
}
