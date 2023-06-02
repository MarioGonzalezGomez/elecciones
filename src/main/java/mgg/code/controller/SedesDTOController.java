package mgg.code.controller;


import mgg.code.model.CP;
import mgg.code.model.Key;
import mgg.code.model.Partido;
import mgg.code.model.dto.SedesDTO;
import mgg.code.model.dto.mapper.SedesDTOMapper;
import mgg.code.service.ficheros.CsvExportService;
import mgg.code.service.ficheros.ExcelExportService;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

public class SedesDTOController {
    public static SedesDTOController controller;

    private PartidoController parCon;
    private CPController cpCon;
    private CsvExportService csvExportService;

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

    public void getSedesDTOInCsv(String partido) {
        SedesDTO dto = findById(partido);
        //TODO:Hacer BufferedWriter al fichero que sea y pasarlo como segundo parámetro a csvExportService
        //csvExportService.writeSedesDTOToCsv(dto);
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
