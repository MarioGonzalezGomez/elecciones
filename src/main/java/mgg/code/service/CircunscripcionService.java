package mgg.code.service;

import mgg.code.model.Circunscripcion;
import mgg.code.repository.CircunscripcionRepository;

import java.util.List;

public class CircunscripcionService extends BaseService<Circunscripcion, String, CircunscripcionRepository> {
    public CircunscripcionService(CircunscripcionRepository repository) {
        super(repository);
    }

    public List<Circunscripcion> getAllCircunscripciones() {
        return this.findAll();
    }

    public Circunscripcion getCircunscripcionById(String id) {
        return this.getById(id);
    }

    public Circunscripcion postCircunscripcion(Circunscripcion Circunscripcion) {
        return this.save(Circunscripcion);
    }

    public Circunscripcion updateCircunscripcion(Circunscripcion Circunscripcion) {
        return this.update(Circunscripcion);
    }

    public Circunscripcion deleteCircunscripcion(Circunscripcion Circunscripcion) {
        return this.delete(Circunscripcion.getCodigo());
    }
}