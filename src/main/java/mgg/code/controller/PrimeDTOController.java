package mgg.code.controller;


import mgg.code.config.Config;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.PrimeDTO;
import mgg.code.model.dto.mapper.PrimeDTOMapper;
import mgg.code.service.ficheros.ExcelExportService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

public class PrimeDTOController {
    private static PrimeDTOController controller = null;
    private PartidoController parCon;
    private CircunscripcionController cirCon;
    private CPController cpCon;

    private final String ruta = Config.getConfiguracion().getRutaFicheros();

    private PrimeDTOController() {
        parCon = PartidoController.getInstance();
        cirCon = CircunscripcionController.getInstance();
        cpCon = CPController.getInstance();
    }

    public static PrimeDTOController getInstance() {
        if (controller == null) {
            controller = new PrimeDTOController();
        }
        return controller;
    }

    public List<PrimeDTO> findAll() {
        PrimeDTOMapper mapper = new PrimeDTOMapper();
        List<PrimeDTO> listado = new ArrayList<>();
        List<Circunscripcion> circunscripciones = cirCon.getAllCircunscripciones().stream().filter(cir -> cir.getCodigo().endsWith("00000")).toList();
        List<Partido> partidos = parCon.getAllPartidos();
        circunscripciones.forEach(cir -> {
            List<CP> cps = cpCon.findByIdCircunscripcionOficial(cir.getCodigo());
            PrimeDTO dto = mapper.toDTO(cir, cps, partidos);
            listado.add(dto);
        });

        return listado;
    }

    public void findAllInExcel(List<PrimeDTO> primes) throws IOException {
        ExcelExportService excelExportService = new ExcelExportService();
        FileOutputStream outputStream = new FileOutputStream(ruta+ "\\PrimeData.xlsx");
        excelExportService.writeToExcel((RandomAccess) primes, 6,outputStream);
    }
}
