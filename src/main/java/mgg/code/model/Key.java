package mgg.code.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Key implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "COD_PARTIDO")
    private String partido;

    @Column(name = "COD_CIRCUNSCRIPCION")
    private String circunscripcion;
}
