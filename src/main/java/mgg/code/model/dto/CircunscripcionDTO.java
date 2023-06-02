package mgg.code.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CircunscripcionDTO {

    private String codigo;
    private String codigoComunidad;
    private String codigoProvincia;
    private String codigoMunicipio;
    private String nombreCircunscripcion;
    private double escrutado;
    private int escanios;
    private double participacion;
    private double participacionHistorico;
    private double participacionMedia;
    private String avanceActual;
    private int votantes;
    private int escaniosHistoricos;
    private String anioUltimosDatos;
    private int mayoria;
    private int votantesHistoricos;
    private double participacionHist;
}

