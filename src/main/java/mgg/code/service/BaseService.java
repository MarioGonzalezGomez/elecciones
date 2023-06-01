package mgg.code.service;

import lombok.RequiredArgsConstructor;
import mgg.code.repository.CrudRepository;

import java.util.List;

@RequiredArgsConstructor // Requerimos un constructor con al menos las propiedades finales
public abstract class BaseService<T, String, R extends CrudRepository<T, String>> {
    protected final R repository;

    // Operaciones CRUD

    // Obtiene todos
    public List<T> findAll() {
        return repository.findAll();
    }

    // Obtiene por ID
    public T getById(String d) {
        return repository.getById(d);
    }

    // Salva
    public T save(T t) {
        return repository.save(t);
    }

    // Actualiza
    public T update(T t) {
        return repository.update(t);
    }

    // Elimina
    public T delete(String t) {
        return repository.delete(t);
    }


}
