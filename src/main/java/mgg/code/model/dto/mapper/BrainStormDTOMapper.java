package mgg.code.model.dto.mapper;

import lombok.NoArgsConstructor;
import mgg.code.model.CP;
import mgg.code.model.Circunscripcion;
import mgg.code.model.Partido;
import mgg.code.model.dto.BrainStormDTO;
import mgg.code.model.dto.CircunscripcionDTO;
import mgg.code.model.dto.CpDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BrainStormDTOMapper {
    private final int gradosTotales = 180;
    private ArrayList<Double> posicionesIniciales;
    private ArrayList<Double> posicionesFinales;
    private ArrayList<Double> sumatorios;

    private String avance;

    public BrainStormDTOMapper(String avance) {
        this.avance = avance;
    }

    public BrainStormDTO toDTO(Circunscripcion c, Circunscripcion espania, List<CP> cp, List<Partido> p) {
        CircunscripcionDTOMapper mapper = new CircunscripcionDTOMapper(avance);
        CircunscripcionDTO cdto = mapper.toDTO(c, espania);
        posicionesIniciales = new ArrayList<>();
        posicionesIniciales.add(0.0);
        posicionesIniciales.add(0.0);
        posicionesIniciales.add(0.0);
        posicionesFinales = new ArrayList<>();
        posicionesFinales.add(0.0);
        posicionesFinales.add(0.0);
        posicionesFinales.add(0.0);
        sumatorios = getSumatorios(cp);

        List<CpDTO> cpDTO = new ArrayList<>();
        cp.forEach(x -> {
            Partido pTemp = p.stream().filter(y -> y.getCodigo().equals(x.getId().getPartido())).toList().get(0);
            ArrayList<Double> aperturas = getAperturasArco(x);
            CpDTO cpTemp = CpDTO.builder()
                    .escanos_desde(x.getEscanos_desde())
                    .escanos_hasta(x.getEscanos_hasta())
                    .escanos_hasta_hist(x.getEscanos_hasta_hist())
                    .numVotantes(x.getNumVotantes())
                    .porcentajeVoto(x.getPorcentajeVoto())
                    .porcentajeVotoHistorico(x.getVotantesHistorico())
                    .siglas(pTemp.getSiglas())
                    .literalPartido(pTemp.getNombreCompleto())
                    .codigoPartido(pTemp.getCodigo())
                    .codigoPadre(pTemp.getCodigoPadre())
                    .escanos_desde_sondeo(x.getEscanos_desde_sondeo())
                    .escanos_hasta_sondeo(x.getEscanos_hasta_sondeo())
                    .porcentajeVotoSondeo(x.getPorcentajeVotoSondeo())
                    .posicionInicial(posicionesIniciales.get(0))
                    .aperturaArco(aperturas.get(0))
                    .numVotantesHistoricos(x.getNumVotantesHistorico())
                    .posicionInicialDesdeSondeo(posicionesIniciales.get(1))
                    .aperturaArcoDesdeSondeo(aperturas.get(1))
                    .posicionInicialHastaSondeo(posicionesIniciales.get(2))
                    .aperturaArcoHastaSondeo(aperturas.get(2))
                    .build();
            cpDTO.add(cpTemp);
            posicionesIniciales.clear();
            posicionesIniciales.addAll(posicionesFinales);
        });

        return BrainStormDTO.builder()
                .circunscripcion(cdto)
                .numPartidos(cp.size())
                .cpDTO(cpDTO)
                .build();
    }

    private ArrayList<Double> getSumatorios(List<CP> cps) {
        ArrayList<Double> sumatorios = new ArrayList<>();
        double sumatorioHasta = cps.stream().mapToInt(CP::getEscanos_hasta).sum();
        // double sumatorioDesdeSondeo = cps.stream().mapToInt(CircunscripcionPartido::getEscanos_desde_sondeo).sum();
        double sumatorioHastaSondeo = cps.stream().mapToInt(CP::getEscanos_hasta_sondeo).sum();
        sumatorios.add(sumatorioHasta);
        //Se añade una segunda vez, ya que la apertura de los "Desde" tomará también como total el sumatorio de los hasta
        sumatorios.add(sumatorioHastaSondeo);
        //sumatorios.add(sumatorioDesdeSondeo);
        sumatorios.add(sumatorioHastaSondeo);

        return sumatorios;
    }

    private ArrayList<Double> getAperturasArco(CP cp) {
        ArrayList<Double> aperturas = new ArrayList<>();
        double aperturaOficial = 0.0;
        double aperturaDesdeSondeo = 0.0;
        double aperturaHastaSondeo = 0.0;
        if (sumatorios.get(0) != 0.0)
            aperturaOficial = cp.getEscanos_hasta() * gradosTotales / sumatorios.get(0);
        if (sumatorios.get(1) != 0.0)
            aperturaDesdeSondeo = cp.getEscanos_desde_sondeo() * gradosTotales / sumatorios.get(1);
        if (sumatorios.get(2) != 0.0)
            aperturaHastaSondeo = cp.getEscanos_hasta_sondeo() * gradosTotales / sumatorios.get(2);

        posicionesFinales.set(0, posicionesIniciales.get(0) + aperturaOficial);
        posicionesFinales.set(1, posicionesIniciales.get(1) + aperturaDesdeSondeo);
        posicionesFinales.set(2, posicionesIniciales.get(2) + aperturaHastaSondeo);
        aperturas.add(aperturaOficial);
        aperturas.add(aperturaDesdeSondeo);
        aperturas.add(aperturaHastaSondeo);
        return aperturas;
    }
}
