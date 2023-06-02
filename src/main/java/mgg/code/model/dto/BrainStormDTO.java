package mgg.code.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrainStormDTO {
    private CircunscripcionDTO circunscripcion;
    private int numPartidos;
    private List<CpDTO> cpDTO;
}
