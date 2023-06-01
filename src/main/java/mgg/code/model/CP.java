package mgg.code.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "circunscripcion_partido")
public class CP {

    @EmbeddedId
    private Key id;

    @Column
    private int escanos_desde;

    @Column
    private int escanos_hasta;

    @Column(name = "votos")
    private double porcentajeVoto;

    @Column(name = "votantes")
    private int numVotantes;

    @Column
    private int escanos_desde_hist;

    @Column
    private int escanos_hasta_hist;

    @Column(name = "votos_hist")
    private double votantesHistorico;

    @Column(name = "votantes_hist")
    private int numVotantesHistorico;

    @Column
    private int escanos_desde_sondeo;

    @Column
    private int escanos_hasta_sondeo;

    @Column(name = "votos_sondeo")
    private double porcentajeVotoSondeo;

}
