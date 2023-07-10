package mgg.code.controller;

import mgg.code.config.Config;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.mapper.BrainStormDTOMapper;
import mgg.code.service.ficheros.CsvExportService;
import mgg.code.service.ficheros.ExcelExportService;
import mgg.code.util.comparators.CPOficial;
import mgg.code.util.comparators.CPSondeo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BrainStormDTOController {

    public static BrainStormDTOController controller;
    private final String ruta = Config.getConfiguracion().getRutaFicheros();

    private PartidoController parCon;
    private CircunscripcionController cirCon;
    private CPController cpCon;
    private CsvExportService csvExportService;

    private BrainStormDTOController() {
        parCon = PartidoController.getInstance();
        cirCon = CircunscripcionController.getInstance();
        cpCon = CPController.getInstance();
        csvExportService = CsvExportService.getInstance();
    }

    public static BrainStormDTOController getInstance() {
        if (controller == null) {
            controller = new BrainStormDTOController();
        }
        return controller;
    }

    public BrainStormDTO getBrainStormDTOOficial(String cod1, String avance) {
        Circunscripcion circunscripcion = cirCon.getCircunscripcionById(cod1);
        Circunscripcion espania = cirCon.getCircunscripcionById("9900000");
        List<CP> cp =
                cpCon.findByIdCircunscripcionOficial(cod1).stream()
                        .filter(x -> x.getEscanos_hasta() > 0)
                        .sorted(new CPOficial().reversed())
                        .collect(Collectors.toList());

        List<Partido> partidos = new ArrayList<>();
        cp.forEach(x -> partidos.add(parCon.getPartidoById(x.getId().getPartido())));
        BrainStormDTOMapper mapper = new BrainStormDTOMapper(avance);
        BrainStormDTO dto = mapper.toDTO(circunscripcion, espania, cp, partidos);
        return dto;
    }

    public BrainStormDTO getBrainStormDTOSenado(String cod1, String avance) {
        Circunscripcion circunscripcion = cirCon.getCircunscripcionByIdSenado(cod1);
        Circunscripcion espania = cirCon.getCircunscripcionByIdSenado("9900000");
        List<CP> cp =
                cpCon.findByIdCircunscripcionSenado(cod1).stream()
                        .filter(x -> x.getEscanos_hasta() > 0)
                        .sorted(new CPOficial().reversed())
                        .collect(Collectors.toList());

        List<Partido> partidos = new ArrayList<>();
        cp.forEach(x -> partidos.add(parCon.getPartidoByIdSenado(x.getId().getPartido())));
        BrainStormDTOMapper mapper = new BrainStormDTOMapper(avance);
        BrainStormDTO dto = mapper.toDTO(circunscripcion, espania, cp, partidos);
        return dto;
    }

    public BrainStormDTO getBrainStormDTOSondeo(String cod1, String avance) {
        Circunscripcion circunscripcion = cirCon.getCircunscripcionById(cod1);
        Circunscripcion espania = cirCon.getCircunscripcionById("9900000");
        List<CP> cp
                = cpCon.findByIdCircunscripcionSondeo(cod1).stream()
                .filter(x -> x.getEscanos_hasta_sondeo() > 0)
                .sorted(new CPSondeo().reversed())
                .collect(Collectors.toList());

        List<Partido> partidos = new ArrayList<>();
        cp.forEach(x -> partidos.add(parCon.getPartidoById(x.getId().getPartido())));
        BrainStormDTOMapper mapper = new BrainStormDTOMapper(avance);
        BrainStormDTO dto = mapper.toDTO(circunscripcion, espania, cp, partidos);

        return dto;
    }


    public void getBrainStormDTOOficialCongresoInCsv(String cod1, String avance){
        BrainStormDTO dto = getBrainStormDTOOficial(cod1, avance);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(ruta + "\\F_Congreso.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        csvExportService.writeBrainStormDTOToCsv(dto, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBrainStormDTOSenadoInCsv(String cod1, String avance) {
        BrainStormDTO dto = getBrainStormDTOSenado(cod1, avance);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(ruta + "\\F_Senado.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        csvExportService.writeBrainStormDTOToCsv(dto, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBrainStormDTOSondeoInCsv(String cod1, String avance) {
        BrainStormDTO dto = getBrainStormDTOSondeo(cod1, avance);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(ruta + "\\F_Congreso_Sondeo.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        csvExportService.writeBrainStormDTOToCsv(dto, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBrainStormDTOSondeoEspecialInCsv(String cod1, String avance){
        BrainStormDTO dto = getBrainStormDTOSondeo(cod1, avance);
        dto.getCpDTO().forEach(cp -> {
            cp.setEscanos_desde(cp.getEscanos_desde_sondeo());
            cp.setEscanos_hasta(cp.getEscanos_hasta_sondeo());
            cp.setPorcentajeVoto(cp.getPorcentajeVotoSondeo());
        });
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(ruta + "\\F_Congreso_Sondeo.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        csvExportService.writeBrainStormDTOToCsv(dto, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getBrainStormDTOInExcel(String cod1, String avance) {
        BrainStormDTO dto = getBrainStormDTOOficial(cod1, avance);
        List<BrainStormDTO> listado = new ArrayList<>();
        listado.add(dto);
        ExcelExportService excelExportService = new ExcelExportService();
        // excelExportService.writeToExcel((RandomAccess) listado, 4);
    }

    public void getBrainStormDTOSondeoInExcel(String cod1, String avance) {
        BrainStormDTO dto = getBrainStormDTOSondeo(cod1, avance);
        List<BrainStormDTO> listado = new ArrayList<>();
        listado.add(dto);
        ExcelExportService excelExportService = new ExcelExportService();
        //TODO:¿Add writer?
        //excelExportService.writeToExcel((RandomAccess) listado, 4);
    }
}
