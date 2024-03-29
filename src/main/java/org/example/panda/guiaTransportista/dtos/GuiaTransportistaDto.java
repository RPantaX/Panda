package org.example.panda.guiaTransportista.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.example.panda.aplicationSecurity.persistence.entities.User;
import org.example.panda.trabajador.entities.Trabajador;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuiaTransportistaDto implements Serializable {

    private Integer id;
    private String serieGuia;

    private Integer numeroGuia;

    private String partida;

    private String llegada;

    private Timestamp fechaEmision;


    private Date fechaTraslado;

    private String remitenteRuc;

    private String remitenteRazonSocial;

    private String remitenteDireccion;

    private String destinatarioRuc;

    private String destinatarioRazonSocial;

    private String destinatarioDireccion;

    private BigDecimal pesoCarga;

    private String numDocChofer;

    private String nombreChofer;

    private String placaVehiculo;

    private String rucPagadorDelFlete;

    private User user;
}