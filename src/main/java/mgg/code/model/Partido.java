package mgg.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedQuery;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partidos")
@NamedQuery(name = "Partido.findAll", query = "SELECT a FROM Partido a")
public class Partido {

    @Id
    @Column(name = "PARTIDO")
    private String codigo;

    @Column(name = "sigla")
    private String siglas;

    @Column(name = "padre")
    private String codigoPadre;

    @Column(name = "descripcion")
    private String nombreCompleto;
}
