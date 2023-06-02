package mgg.code.controller;

import mgg.code.model.Partido;
import mgg.code.repository.PartidoRepository;
import mgg.code.service.PartidoService;

import java.io.IOException;
import java.util.List;

public class PartidoController {
    private static PartidoController controller = null;
    private final PartidoService service;

    private PartidoController(PartidoService service) {
        this.service = service;
    }

    public static PartidoController getInstance() {
        if (controller == null) {
            controller = new PartidoController(new PartidoService(new PartidoRepository()));
        }
        return controller;
    }

    public List<Partido> getAllPartidos() {
        return service.getAllPartidos();
    }

    public void getAllPartidosInCsv() throws IOException {
        service.getAllPartidosInCsv();
    }

    public Partido getPartidoById(String id) {
        return service.getPartidoById(id);
    }

    public void getPartidoByIdinCsv(String id) throws IOException {service.getPartidoByIdInCsv(id);}

    public Partido postPartido(Partido Partido) {
        return service.postPartido(Partido);
    }

    public Partido updatePartido(Partido Partido) {
        return service.updatePartido(Partido);
    }

    public Partido deletePartido(Partido Partido) {
        return service.deletePartido(Partido);

    }
}
