package mgg.code.repository;

import java.util.List;

public interface CrudRepository<T, String> {

    // Operaciones CRUD

    // Obtiene todos
    List<T> findAll();

    // Obtiene por ID
    T getById(String id);

    // Salva
    T save(T t);

    // Actualiza
    T update(T t);

    // Elimina
    T delete(String id);


}
