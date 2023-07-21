package mgg.code.model.dto.mapper;

import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CpDTO;
import mgg.code.model.dto.CpPrimeDTO;
import mgg.code.model.dto.PrimeDTO;
import mgg.code.util.LecturaFicheroColores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimeDTOMapper {


    public PrimeDTO toDTO(Circunscripcion c, List<CP> cp, List<Partido> p) {
        Map<String, String> colores = crearMapaPartidos();
        List<CpPrimeDTO> primes = new ArrayList<>();
        cp.forEach(x -> {
            Partido pTemp = p.stream().filter(y -> y.getCodigo().equals(x.getId().getPartido())).toList().get(0);
            String color = colores.getOrDefault(pTemp.getCodigo(), "#000000");
            CpPrimeDTO cpTemP = CpPrimeDTO.builder()
                    .codigoPartido(pTemp.getCodigo())
                    .escanios(x.getEscanos_hasta())
                    .escaniosHistorico(x.getEscanos_hasta_hist())
                    .color(color)
                    .votantes(String.format("%,d", x.getNumVotantes()))
                    .build();
            primes.add(cpTemP);
        });

        return PrimeDTO.builder()
                .nombreCircunscripcion(c.getNombreCircunscripcion())
                .codigoCircunscripcion(c.getCodigo())
                .escaniosTotales(c.getEscanios())
                .escrutado(c.getEscrutado())
                .cps(primes)
                .build();
    }

    public PrimeDTO fromBS(BrainStormDTO bs) {

        return PrimeDTO.builder()
                .nombreCircunscripcion(bs.getCircunscripcion().getNombreCircunscripcion())
                .codigoCircunscripcion(bs.getCircunscripcion().getCodigo())
                .escrutado(bs.getCircunscripcion().getEscrutado())
                .escaniosTotales(bs.getCircunscripcion().getEscanios())
                .cps(fromCPDTO(bs.getCpDTO()))
                .build();
    }

    private List<CpPrimeDTO> fromCPDTO(List<CpDTO> cps) {
        ArrayList<CpPrimeDTO> listado = new ArrayList<>();
        cps.forEach(cp -> {
            CpPrimeDTO temp = CpPrimeDTO.builder()
                    .votantes(String.format("%,d", cp.getNumVotantes()))
                    .codigoPartido(cp.getCodigoPartido())
                    .color("#FFFFFF")
                    .escaniosHistorico(cp.getEscanos_hasta_hist())
                    .escanios(cp.getEscanos_hasta())
                    .build();
            listado.add(temp);
        });
        return listado;
    }

    private Map<String, String> crearMapaPartidos() {
        LecturaFicheroColores lfc = new LecturaFicheroColores();
        ArrayList<String[]> partidos = lfc.leerFicheroColores();
        Map<String, String> mapaPartidos = new HashMap<>();
        for (String[] partido : partidos) {
            mapaPartidos.put(partido[0], partido[4]);
        }
        return mapaPartidos;
    }
}
