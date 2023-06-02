package mgg.code.model.dto.mapper;


import mgg.code.model.CP;
import mgg.code.model.Partido;
import mgg.code.model.dto.SedesDTO;

public class SedesDTOMapper {

    public SedesDTO toDTO(CP cp, Partido p) {

        return SedesDTO.builder()
                .codigoPartido(p.getCodigo())
                .codigoPadre(p.getCodigoPadre())
                .escanos_desde(cp.getEscanos_desde())
                .escanos_hasta(cp.getEscanos_hasta())
                .escanos_hist(cp.getEscanos_hasta_hist())
                .porcentajeVoto(cp.getPorcentajeVoto())
                .porcentajeVotoHistorico(cp.getVotantesHistorico())
                .numVotantes(cp.getNumVotantes())
                .siglas(p.getSiglas())
                .literalPartido(p.getNombreCompleto())
                .numVotantes_hist(cp.getNumVotantesHistorico())
                .build();
    }

}
