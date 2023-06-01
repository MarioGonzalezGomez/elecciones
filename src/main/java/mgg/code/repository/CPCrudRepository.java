package mgg.code.repository;

import java.util.List;

public interface CPCrudRepository<T, Key> {

    // Operaciones CRUD

    // Obtiene todos
    List<T> findAll();

    // Obtiene por ID
    T getById(Key key);

    // Salva
    T save(T t);

    // Actualiza
    T update(T t);

    // Elimina
    T delete(Key key);


}
