package mgg.code.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CpPrimeDTO {
    private String codigoPartido;
    private int escanios;
    private int escaniosHistorico;
    private String color;
    private String votantes;
}
