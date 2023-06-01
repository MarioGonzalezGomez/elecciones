package mgg.code.controller;

import mgg.code.model.Circunscripcion;
import mgg.code.repository.CircunscripcionRepository;
import mgg.code.service.CircunscripcionService;

import java.util.List;

public class CircunscripcionController {
    private static CircunscripcionController controller = null;
    private final CircunscripcionService service;

    private CircunscripcionController(CircunscripcionService service) {
        this.service = service;
    }

    public static CircunscripcionController getInstance() {
        if (controller == null) {
            controller = new CircunscripcionController(new CircunscripcionService(new CircunscripcionRepository()));
        }
        return controller;
    }

    public List<Circunscripcion> getAllCircunscripcions() {
        return service.getAllCircunscripciones();
    }

    public Circunscripcion getCircunscripcionById(String id) {
        return service.getCircunscripcionById(id);
    }

    public Circunscripcion postCircunscripcion(Circunscripcion Circunscripcion) {
        return service.postCircunscripcion(Circunscripcion);
    }

    public Circunscripcion updateCircunscripcion(Circunscripcion Circunscripcion) {
        return service.updateCircunscripcion(Circunscripcion);
    }

    public Circunscripcion deleteCircunscripcion(Circunscripcion Circunscripcion) {
        return service.deleteCircunscripcion(Circunscripcion);
    }
}
