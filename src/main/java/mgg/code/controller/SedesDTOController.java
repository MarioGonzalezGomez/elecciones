package mgg.code.controller;


import mgg.code.config.Config;
import mgg.code.model.CP;
import mgg.code.model.Key;
import mgg.code.model.Partido;
import mgg.code.model.dto.SedesDTO;
import mgg.code.model.dto.mapper.SedesDTOMapper;
import mgg.code.service.ficheros.CsvExportService;
import mgg.code.service.ficheros.ExcelExportService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SedesDTOController {
    public static SedesDTOController controller;

    private PartidoController parCon;
    private CPController cpCon;
    private CsvExportService csvExportService;
    private final String ruta = Config.getConfiguracion().getRutaFicheros();

    private SedesDTOController() {
        parCon = PartidoController.getInstance();
        cpCon = CPController.getInstance();
        csvExportService = CsvExportService.getInstance();
    }

    public static SedesDTOController getInstance() {
        if (controller == null) {
            controller = new SedesDTOController();
        }
        return controller;
    }

    public SedesDTO findById(String codPartido) {
        Partido p = parCon.getPartidoById(codPartido);
        Key key = new Key();
        key.setCircunscripcion("9900000");
        key.setPartido(codPartido);
        CP cp = cpCon.getCPById(key);
        SedesDTOMapper mapper = new SedesDTOMapper();
        SedesDTO dto = mapper.toDTO(cp, p);
        return dto;
    }

    public void getSedesDTOInCsv(SedesDTO sede) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(ruta + "\\F_Sedes.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        csvExportService.writeSedesDTOToCsv(sede, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getSedesDTOInExcel(String partido) {

        SedesDTO dto = findById(partido);
        List<SedesDTO> listado = new ArrayList<>();
        listado.add(dto);
        ExcelExportService excelExportService = new ExcelExportService();
        //TODO:Debería mandar un writer del mismo modo en que lo hago para csv??
        //excelExportService.writeToExcel((RandomAccess) listado, 5);
    }
}
