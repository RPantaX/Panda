package org.example.panda.citas.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.panda.conductor.entities.Conductor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "citas_viaje")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitasViaje implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "origen", nullable = false, length = 100)
    private String origen;

    @Column(name = "destino", nullable = false, length = 100)
    private String destino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "conductor_id", nullable = false)
    private Conductor conductor;
}
