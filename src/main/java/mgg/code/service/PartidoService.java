package mgg.code.service;


import mgg.code.model.Partido;
import mgg.code.repository.PartidoRepository;

import java.util.List;

public class PartidoService extends BaseService<Partido, String, PartidoRepository> {
    public PartidoService(PartidoRepository repository) {
        super(repository);
    }

    public List<Partido> getAllPartidos() {
        return this.findAll();
    }

    public Partido getPartidoById(String id) {
        return this.getById(id);
    }

    public Partido postPartido(Partido partido) {
        return this.save(partido);
    }

    public Partido updatePartido(Partido partido) {
        return this.update(partido);
    }

    public Partido deletePartido(Partido partido) {
        return this.delete(partido.getCodigo());
    }

}
