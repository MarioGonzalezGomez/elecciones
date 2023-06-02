package mgg.code.service;

import mgg.code.config.Config;
import mgg.code.model.Circunscripcion;
import mgg.code.repository.CircunscripcionRepository;
import mgg.code.service.ficheros.CsvExportService;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CircunscripcionService extends BaseService<Circunscripcion, String, CircunscripcionRepository> {
    public CircunscripcionService(CircunscripcionRepository repository) {
        super(repository);
    }

    private final String ruta = Config.getRutaFicheros();
    private final CsvExportService csvExport = CsvExportService.getInstance();

    public List<Circunscripcion> getAllCircunscripciones() {
        return this.findAll();
    }

    public void getAllCircunscripcionesInCsv() throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\Circunscripciones.csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        csvExport.writeCircunscripcionToCsv(getAllCircunscripciones(), bw);
    }

    public void getAllCircunscripcionesInExcel() throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/Circunscripciones/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\Circunscripciones.xlsx"));
    }

    public Circunscripcion getCircunscripcionById(String id) {
        return this.getById(id);
    }

    public void getCircunscripcionByIdInCsv(String id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\Circunscripcion_" + id + ".csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        List<Circunscripcion> Circunscripcions = new ArrayList<>();
        Circunscripcions.add(getCircunscripcionById(id));
        csvExport.writeCircunscripcionToCsv(Circunscripcions, bw);
    }

    public void getCircunscripcionByIdInExcel(String id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/Circunscripcions/" + id + "/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\Circunscripcion_" + id + ".xlsx"));
    }


    public Circunscripcion postCircunscripcion(Circunscripcion Circunscripcion) {
        return this.save(Circunscripcion);
    }

    public Circunscripcion updateCircunscripcion(Circunscripcion Circunscripcion) {
        return this.update(Circunscripcion);
    }

    public Circunscripcion deleteCircunscripcion(Circunscripcion Circunscripcion) {
        return this.delete(Circunscripcion.getCodigo());
    }

    private File comprobarCarpetas() {
        File datos = new File(ruta);
        if (!datos.exists()) {
            datos.mkdir();
        }
        return datos;
    }
}