package mgg.code.service.ficheros;

import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CpDTO;
import mgg.code.model.dto.PrimeDTO;
import mgg.code.model.dto.SedesDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.atomic.AtomicInteger;


public class ExcelExportService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String nombrePagina;

    private int rowCount;


    public ExcelExportService() {
        // Inicializar un nuevo libro de trabajo
        workbook = new XSSFWorkbook();
    }

    //Utilizo un int para identificar el tipo de objeto de la lista y así puedo
    //Hacer estos métodos de forma genérica.
    //Tomaré el 1 como partido, 2 como circunscripcion y 3 como CP (Puede que
    //en el futuro utilice 4 o + para DTO específicos

    public void writeToExcel(RandomAccess listado, int tipoDato, OutputStream out) throws IOException {
        nombrarPagina(tipoDato);
        writeHeader(nombrePagina, tipoDato, 1);
        write(listado, tipoDato);
        workbook.write(out);
        workbook.close();
    }

    private void nombrarPagina(int tipoDato) {
        switch (tipoDato) {
            case 1 -> nombrePagina = "Partidos";
            case 2 -> nombrePagina = "Circunscripciones";
            case 3 -> nombrePagina = "Circunscripcion-Partido";
            case 4 -> nombrePagina = "BrainStormDTO";
            case 5 -> nombrePagina = "Sede";
            case 6 -> nombrePagina = "Prime";
            default -> nombrePagina = "Pag1";
        }
    }

    private void writeHeader(String nombrePagina, int tipoDato, int paginaActual) {
        sheet = workbook.createSheet(nombrePagina + " " + paginaActual);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        switch (tipoDato) {
            case 1 -> {
                createCell(row, 0, "CODIGO", style);
                createCell(row, 1, "SIGLAS", style);
                createCell(row, 2, "CODIGO PADRE", style);
                createCell(row, 3, "LITERAL", style);
            }
            case 2 -> {
                createCell(row, 0, "CODIGO", style);
                createCell(row, 1, "COMUNIDAD AUTONOMA", style);
                createCell(row, 2, "PROVINCIA", style);
                createCell(row, 3, "MUNICIPIO", style);
                createCell(row, 4, "NOMBRE", style);
                createCell(row, 5, "ESCRUTADO", style);
                createCell(row, 6, "ESCANIOS", style);
                createCell(row, 7, "AVANCE 1", style);
                createCell(row, 8, "AVANCE 2", style);
                createCell(row, 9, "AVANCE 3", style);
                createCell(row, 10, "PARTICIPACION", style);
                createCell(row, 11, "VOTANTES", style);
                createCell(row, 12, "ESCANIOS HISTORICO", style);
                createCell(row, 13, "AVANCE 1 HISTORICO", style);
                createCell(row, 14, "AVANCE 2 HISTORICO", style);
                createCell(row, 15, "AVANCE 3 HISTORICO", style);
                createCell(row, 16, "PARTICIPACION HISTORICO", style);
            }
            case 3 -> {
                createCell(row, 0, "CIRCUNSCRIPCION", style);
                createCell(row, 1, "PARTIDO", style);
                createCell(row, 2, "ESCANIOS DESDE", style);
                createCell(row, 3, "ESCANIOS HASTA", style);
                createCell(row, 4, "PORCENTAJE VOTO", style);
                createCell(row, 5, "VOTANTES", style);
                createCell(row, 6, "ESCANIOS HISTORICOS", style);
                createCell(row, 7, "PORCENTAJE VOTO HISTORICO", style);
                createCell(row, 8, "VOTANTES HISTORICO", style);
                createCell(row, 9, "ESCANIOS DESDE SONDEO", style);
                createCell(row, 10, "ESCANIOS HASTA SONDEO", style);
                createCell(row, 11, "PORCENTAJE VOTO SONDEO", style);
            }
            case 4 -> {
                createCell(row, 0, "COD CIRCUNSCRIPCION", style);
                createCell(row, 1, "AUTONOMIA", style);
                createCell(row, 2, "PROVINCIA", style);
                createCell(row, 3, "MUNICIPIO", style);
                createCell(row, 4, "NOMBRE", style);
                createCell(row, 5, "ESCRUTADO ACTUAL", style);
                createCell(row, 6, "ESCANIOS TOTALES", style);
                createCell(row, 7, "PARTICIPACION", style);
                createCell(row, 8, "PARTICIPACION HISTORICA", style);
                createCell(row, 9, "PARTICIPACION MEDIA", style);
                createCell(row, 10, "LITERAL PARTICIPACION", style);
                createCell(row, 11, "VOTANTES", style);
                createCell(row, 12, "ESCANIOS HISTORICO", style);
                createCell(row, 13, "ANIO ULTIMAS ELECCIONES", style);
                createCell(row, 14, "SIN USO", style);
                createCell(row, 15, "SIN USO", style);
                createCell(row, 16, "SIN USO", style);
                createCell(row, 17, "PARTIDOS CON ESCANIO", style);
                createCell(row, 18, "TIPO ELECCIONES", style);
            }
            case 5 -> {
                createCell(row, 0, "CODIGO", style);
                createCell(row, 1, "CODIGO PADRE", style);
                createCell(row, 2, "ESCANIOS DESDE", style);
                createCell(row, 3, "ESCANIOS HASTA", style);
                createCell(row, 4, "ESCANIOS HISTORICO", style);
                createCell(row, 5, "PORCENTAJE VOTO", style);
                createCell(row, 6, "PORCENTAJE HISTORICO", style);
                createCell(row, 7, "VOTANTES", style);
                createCell(row, 8, "SIGLAS", style);
                createCell(row, 9, "LITERAL", style);
                createCell(row, 10, "VOTANTES HISTORICOS", style);
            }
            default -> {
            }
        }

    }

    private void write(RandomAccess listado, int tipoDato) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        rowCount = 1;

        switch (tipoDato) {
            case 1 -> {
                List<Partido> partidos = (List<Partido>) listado;
                //Ejemplo de paginacion
                // if (partidos.size() <= 50) {
                partidos.forEach(x -> createPartido(x, style));
            }
            //  } else if (partidos.size() <= 100) {
            //      List<Partido> parte1 = partidos.subList(0, 50);
            //      parte1.forEach(x -> createPartido(x, style));
            //      writeHeader(nombrePagina, 1, 2);
            //      rowCount = 1;
            //      List<Partido> parte2 = partidos.subList(50, partidos.size() - 1);
            //      parte2.forEach(x -> createPartido(x, style));
            //  } else {
            //      List<Partido> parte1 = partidos.subList(0, 50);
            //      parte1.forEach(x -> createPartido(x, style));
            //      writeHeader(nombrePagina, 1, 2);
            //      rowCount = 1;
            //      List<Partido> parte2 = partidos.subList(50, 100);
            //      parte2.forEach(x -> createPartido(x, style));
            //      writeHeader(nombrePagina, 1, 3);
            //      rowCount = 1;
            //      List<Partido> parte3 = partidos.subList(100, partidos.size() - 1);
            //      parte3.forEach(x -> createPartido(x, style));
            //  }
            case 2 -> {
                List<Circunscripcion> circunscripciones = (List<Circunscripcion>) listado;
                circunscripciones.forEach(x -> createCircunscripcion(x, style));
            }
            case 3 -> {
                List<CP> cps = (List<CP>) listado;
                cps.forEach(x -> createCP(x, style));
            }
            case 4 -> {
                List<BrainStormDTO> BrainStormDTOS = (List<BrainStormDTO>) listado;
                createBrainStormDTO(BrainStormDTOS.get(0), style);
            }
            case 5 -> {
                List<SedesDTO> sedesDTOS = (List<SedesDTO>) listado;
                createSedesDTO(sedesDTOS.get(0), style);
            }
            case 6 -> {
                List<PrimeDTO> primeDTOS = (List<PrimeDTO>) listado;
                int numMax = primeDTOS.stream().max(Comparator.comparingInt(a -> a.getCps().size())).orElse(null).getCps().size();
                primeDTOS.forEach(x -> createPrimeDTO(x, numMax, style));
            }
            default -> {
            }
        }
    }

    private void createPartido(Partido x, CellStyle style) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, x.getCodigo(), style);
        createCell(row, columnCount++, x.getSiglas(), style);
        createCell(row, columnCount++, x.getCodigoPadre(), style);
        createCell(row, columnCount++, x.getNombreCompleto(), style);
    }

    private void createCircunscripcion(Circunscripcion x, CellStyle style) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, x.getCodigo(), style);
        createCell(row, columnCount++, x.getCodigoComunidad(), style);
        createCell(row, columnCount++, x.getCodigoProvincia(), style);
        createCell(row, columnCount++, x.getCodigoMunicipio(), style);

        createCell(row, columnCount++, x.getNombreCircunscripcion(), style);
        createCell(row, columnCount++, x.getEscrutado(), style);
        createCell(row, columnCount++, x.getEscanios(), style);
        createCell(row, columnCount++, x.getAvance1(), style);
        createCell(row, columnCount++, x.getAvance2(), style);
        createCell(row, columnCount++, x.getAvance3(), style);
        createCell(row, columnCount++, x.getParticipacion(), style);
        createCell(row, columnCount++, x.getVotantes(), style);

        createCell(row, columnCount++, x.getEscaniosHistoricos(), style);
        createCell(row, columnCount++, x.getAvance1Hist(), style);
        createCell(row, columnCount++, x.getAvance2Hist(), style);
        createCell(row, columnCount++, x.getAvance3Hist(), style);
        createCell(row, columnCount++, x.getParticipacionHist(), style);
    }

    private void createCP(CP x, CellStyle style) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, x.getId().getCircunscripcion(), style);
        createCell(row, columnCount++, x.getId().getPartido(), style);
        createCell(row, columnCount++, x.getEscanos_desde(), style);
        createCell(row, columnCount++, x.getEscanos_hasta(), style);
        createCell(row, columnCount++, x.getPorcentajeVoto(), style);
        createCell(row, columnCount++, x.getNumVotantes(), style);
        createCell(row, columnCount++, x.getEscanos_desde_hist(), style);
        createCell(row, columnCount++, x.getEscanos_hasta_hist(), style);
        createCell(row, columnCount++, x.getVotantesHistorico(), style);
        createCell(row, columnCount++, x.getNumVotantesHistorico(), style);
        createCell(row, columnCount++, x.getEscanos_desde_sondeo(), style);
        createCell(row, columnCount++, x.getEscanos_hasta_sondeo(), style);
        createCell(row, columnCount++, x.getPorcentajeVotoSondeo(), style);

    }

    //TODO:obtener desde la parte gráfica el dato sobre el tipo de elecciones
    private void createBrainStormDTO(BrainStormDTO x, CellStyle style) {
        Row row = sheet.createRow(rowCount++);
        String tipoElecciones = "Autonómicas";
        int columnCount = 0;
        createCell(row, columnCount++, x.getCircunscripcion().getCodigo(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getCodigoComunidad(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getCodigoProvincia(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getCodigoMunicipio(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getNombreCircunscripcion(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getEscrutado(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getEscanios(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getParticipacion(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getParticipacionHistorico(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getParticipacionMedia(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getAvanceActual(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getVotantes(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getEscaniosHistoricos(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getAnioUltimosDatos(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getMayoria(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getVotantesHistoricos(), style);
        createCell(row, columnCount++, x.getCircunscripcion().getParticipacionHist(), style);
        createCell(row, columnCount++, x.getNumPartidos(), style);
        createCell(row, columnCount++, tipoElecciones, style);


        row = sheet.createRow(rowCount++);

        createCell(row, 0, "COD PARTIDO", style);
        createCell(row, 1, "COD PADRE", style);
        createCell(row, 2, "ESCANIOS DESDE", style);
        createCell(row, 3, "ESCANIOS HASTA", style);
        createCell(row, 4, "ESCANIOS HIST", style);
        createCell(row, 5, "PORCENTAJE VOTO", style);
        createCell(row, 6, "PORCENTAJE VOTO HISTORICO", style);
        createCell(row, 7, "VOTANTES", style);
        createCell(row, 8, "SIGLAS", style);
        createCell(row, 9, "LITERAL", style);
        createCell(row, 10, "DIFERENCIA ESCANIOS", style);
        createCell(row, 11, "TENDENCIA", style);


        List<CpDTO> cpdtos = x.getCpDTO();
        cpdtos.forEach(y -> {
            int diferencia = y.getEscanos_hasta() - y.getEscanos_hasta_hist();
            String tendencia;
            if (diferencia < 0) {
                diferencia = diferencia * (-1);
                tendencia = "-";
            } else if (diferencia == 0) {
                tendencia = "=";
            } else {
                tendencia = "+";
            }
            Row newRow = sheet.createRow(rowCount++);
            int column = 0;
            createCell(newRow, column++, y.getCodigoPartido(), style);
            createCell(newRow, column++, y.getCodigoPadre(), style);
            createCell(newRow, column++, y.getEscanos_desde(), style);
            createCell(newRow, column++, y.getEscanos_hasta(), style);
            createCell(newRow, column++, y.getEscanos_hasta_hist(), style);
            createCell(newRow, column++, y.getPorcentajeVoto(), style);
            createCell(newRow, column++, y.getPorcentajeVotoHistorico(), style);
            createCell(newRow, column++, y.getNumVotantes(), style);
            createCell(newRow, column++, y.getSiglas(), style);
            createCell(newRow, column++, y.getLiteralPartido(), style);
            createCell(newRow, column++, String.valueOf(diferencia), style);
            createCell(newRow, column++, tendencia, style);
        });
    }

    private void createSedesDTO(SedesDTO x, CellStyle style) {
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        createCell(row, columnCount++, x.getCodigoPartido(), style);
        createCell(row, columnCount++, x.getCodigoPadre(), style);
        createCell(row, columnCount++, x.getEscanos_desde(), style);
        createCell(row, columnCount++, x.getEscanos_hasta(), style);
        createCell(row, columnCount++, x.getEscanos_hist(), style);
        createCell(row, columnCount++, x.getPorcentajeVoto(), style);
        createCell(row, columnCount++, x.getPorcentajeVotoHistorico(), style);
        createCell(row, columnCount++, x.getNumVotantes(), style);
        createCell(row, columnCount++, x.getSiglas(), style);
        createCell(row, columnCount++, x.getLiteralPartido(), style);
        createCell(row, columnCount++, x.getNumVotantes_hist(), style);
    }

    private void createPrimeDTO(PrimeDTO x, int numMax, CellStyle style) {
        if (rowCount == 1) {
            Row row = sheet.createRow(0);
            AtomicInteger columnCount = new AtomicInteger(4);
            createCell(row, 0, "CIRCUNSCRIPCION", style);
            createCell(row, 1, "CODIGO CIRCUNSCRIPCION", style);
            createCell(row, 2, "ESCANIOS TOTALES", style);
            createCell(row, 3, "ESCRUTADO", style);
            for (int i = 0; i < numMax; i++) {
                createCell(row, columnCount.getAndIncrement(), "CODIGOS PARTIDO " + (i + 1), style);
                createCell(row, columnCount.getAndIncrement(), "ESCANIOS " + (i + 1), style);
                createCell(row, columnCount.getAndIncrement(), "ESCANIOS HISTORICOS " + (i + 1), style);
                createCell(row, columnCount.getAndIncrement(), "COLORES " + (i + 1), style);
                createCell(row, columnCount.getAndIncrement(), "VOTANTES " + (i + 1), style);
            }
        }

        Row newrow = sheet.createRow(rowCount++);
        AtomicInteger column = new AtomicInteger();

        createCell(newrow, column.getAndIncrement(), x.getNombreCircunscripcion(), style);
        createCell(newrow, column.getAndIncrement(), x.getCodigoCircunscripcion(), style);
        createCell(newrow, column.getAndIncrement(), x.getEscaniosTotales(), style);
        createCell(newrow, column.getAndIncrement(), x.getEscrutado(), style);

        x.getCps().forEach(cp -> {
            createCell(newrow, column.getAndIncrement(), cp.getCodigoPartido(), style);
            createCell(newrow, column.getAndIncrement(), cp.getEscanios(), style);
            createCell(newrow, column.getAndIncrement(), cp.getEscaniosHistorico(), style);
            createCell(newrow, column.getAndIncrement(), cp.getColor(), style);
            createCell(newrow, column.getAndIncrement(), cp.getVotantes(), style);
        });
    }


    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }


}
