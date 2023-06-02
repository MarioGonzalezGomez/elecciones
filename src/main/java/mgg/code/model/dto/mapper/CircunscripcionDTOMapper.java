package mgg.code.model.dto.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import mgg.code.model.Circunscripcion;
import mgg.code.model.dto.CircunscripcionDTO;

@NoArgsConstructor
@AllArgsConstructor
public class CircunscripcionDTOMapper {

    private String avance;

    public CircunscripcionDTO toDTO(Circunscripcion c, Circunscripcion espania) {
        double participacion = 0.0;
        double participacionHistorica = 0.0;
        double participacionMedia = 0.0;
        String literal = "";

        switch (avance) {
            case "1" -> {
                participacion = c.getAvance1();
                participacionHistorica = c.getAvance1Hist();
                participacionMedia = espania.getAvance1();
                literal = "1";
            }
            case "2" -> {
                participacion = c.getAvance2();
                participacionHistorica = c.getAvance2Hist();
                participacionMedia = espania.getAvance2();
                literal = "2";
            }
            case "3" -> {
                participacion = c.getAvance3();
                participacionHistorica = c.getAvance3Hist();
                participacionMedia = espania.getAvance3();
                literal = "3";
            }
            case "4" -> {
                participacion = c.getParticipacion();
                participacionHistorica = c.getParticipacionHist();
                participacionMedia = espania.getParticipacion();
                literal = "4";
            }
        }
        return CircunscripcionDTO.builder()
                .codigo(c.getCodigo())
                .codigoComunidad(c.getCodigoComunidad())
                .codigoProvincia(c.getCodigoProvincia())
                .codigoMunicipio(c.getCodigoMunicipio())
                .nombreCircunscripcion(c.getNombreCircunscripcion())
                .escrutado(c.getEscrutado())
                .escanios(c.getEscanios())
                .participacion(participacion)
                .participacionHistorico(participacionHistorica)
                .avanceActual(literal)
                .participacionMedia(participacionMedia)
                .anioUltimosDatos("2019")
                .votantes(c.getVotantes())
                .escaniosHistoricos(c.getEscaniosHistoricos())
                .mayoria(getMayoria(c.getEscanios()))
                .votantesHistoricos(0)
                .participacionHist(0.0)
                .build();
    }

    private int getMayoria(int escanios) {
        return (escanios / 2) + 1;
    }


}
