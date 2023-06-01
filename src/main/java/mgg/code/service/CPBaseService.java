package mgg.code.service;

import lombok.RequiredArgsConstructor;
import mgg.code.repository.CPCrudRepository;

import java.util.List;

@RequiredArgsConstructor // Requerimos un constructor con al menos las propiedades finales
public abstract class CPBaseService<T, Key, R extends CPCrudRepository<T, Key>> {
    protected final R repository;

    // Operaciones CRUD

    // Obtiene todos
    public List<T> findAll() {
        return repository.findAll();
    }

    // Obtiene por ID
    public T getById(Key key) {
        return repository.getById(key);
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
    public T delete(Key key) {
        return repository.delete(key);
    }


}
