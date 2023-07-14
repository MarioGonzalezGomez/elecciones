package mgg.code.service;

import mgg.code.config.Config;
import mgg.code.model.CP;
import mgg.code.model.Key;
import mgg.code.repository.CPRepository;
import mgg.code.service.ficheros.CsvExportService;
import mgg.code.util.comparators.CPOficial;
import mgg.code.util.comparators.CPSondeo;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CPService extends CPBaseService<CP, Key, CPRepository> {

    private final String ruta = Config.getConfiguracion().getRutaFicheros();
    private final CsvExportService csvExport = CsvExportService.getInstance();

    public CPService(CPRepository repository) {
        super(repository);
    }

    public List<CP> getAllCPs() {
        return this.findAll().stream().filter(x -> x.getEscanos_hasta() > 0)
                .sorted(new CPOficial().reversed()).collect(Collectors.toList());
    }

    public List<CP> getAllCPsSenado() {
        return repository.findAllSenado().stream().filter(x -> x.getEscanos_hasta() > 0)
                .sorted(new CPOficial().reversed()).collect(Collectors.toList());
    }

    public void getAllCPsInCsv() throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\CPs.csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        csvExport.writeCPToCsv(getAllCPs(), bw);
    }

    public void getAllCPesInExcel() throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/CPes/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\CPes.xlsx"));
    }

    public List<CP> getByIdCircunscripcionOficial(String codCircunscripcion) {
        return this.findAll().stream().filter(x -> x.getEscanos_hasta() > 0)
                .filter(x -> x.getId().getCircunscripcion().endsWith(codCircunscripcion))
                .sorted(new CPOficial().reversed()).collect(Collectors.toList());
    }

    public List<CP> getByIdCircunscripcionSenado(String codCircunscripcion) {
        return repository.findAllSenado().stream().filter(x -> x.getEscanos_hasta() > 0)
                .filter(x -> x.getId().getCircunscripcion().endsWith(codCircunscripcion))
                .sorted(new CPOficial().reversed()).collect(Collectors.toList());
    }

    public List<CP> getByIdCircunscripcionSondeo(String codCircunscripcion) {
        return this.findAll().stream().filter(x -> x.getEscanos_hasta_sondeo() > 0)
                .filter(x -> x.getId().getCircunscripcion().endsWith(codCircunscripcion))
                .sorted(new CPSondeo().reversed()).collect(Collectors.toList());
    }

    public CP getCPById(Key key) {
        return this.getById(key);
    }

    public CP getCPByIdSenado(Key key) {
        return repository.getByIdSenado(key);
    }

    public void getCPByIdInCsv(Key id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        String ruta = carpetaBase.getPath() + "\\CP_" + id.getCircunscripcion() + "_" + id.getPartido() + ".csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
        List<CP> CPs = new ArrayList<>();
        CPs.add(getCPById(id));
        csvExport.writeCPToCsv(CPs, bw);
    }

    public void getCPByIdInExcel(String id) throws IOException {
        File carpetaBase = comprobarCarpetas();
        URL url = new URL("http://" + Config.connectedServer + ":8080/autonomicas/CPs/" + id + "/excel");
        FileUtils.copyURLToFile(url, new File(carpetaBase.getPath() + "\\EXCEL\\CP_" + id + ".xlsx"));
    }

    public CP postCP(CP cp) {
        return this.save(cp);
    }

    public CP updateCP(CP cp) {
        return this.update(cp);
    }

    public CP deleteCP(CP cp) {
        return this.delete(cp.getId());
    }

    private File comprobarCarpetas() {
        File datos = new File(ruta);
        if (!datos.exists()) {
            datos.mkdir();
        }
        return datos;
    }
}
