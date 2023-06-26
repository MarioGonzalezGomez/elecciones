package mgg.code.service;


import mgg.code.config.Config;
import mgg.code.model.Partido;
import mgg.code.repository.PartidoRepository;
import mgg.code.service.ficheros.CsvExportService;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PartidoService extends BaseService<Partido, String, PartidoRepository> {

    private final String ruta = Config.getConfiguracion().getRutaFicheros();

    private final CsvExportService csvExport = CsvExportService.getInstance();


    public PartidoService(PartidoRepository repository) {
        super(repository);
    }

    public List<Partido> getAllPartidos() {
        return this.findAll();
    }

    public void getAllPartidosInCsv() throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\partidos.csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        csvExport.writePartidoToCsv(getAllPartidos(), bw);
    }

    public void getAllPartidosInExcel() throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/partidos/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\partidos.xlsx"));
    }

    public Partido getPartidoById(String id) {
        return this.getById(id);
    }

    public void getPartidoByIdInCsv(String id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\partido_" + id + ".csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        List<Partido> partidos = new ArrayList<>();
        partidos.add(getPartidoById(id));
        csvExport.writePartidoToCsv(partidos, bw);
    }

    public void getPartidoByIdInExcel(String id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/partidos/" + id + "/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\partido_" + id + ".xlsx"));
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


    private File comprobarCarpetas() {
        File datos = new File(ruta);
        if (!datos.exists()) {
            datos.mkdir();
        }
        return datos;
    }


}
