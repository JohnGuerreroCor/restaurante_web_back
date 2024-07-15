package com.usco.edu.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Venta implements Serializable {

    private int codigo;
    private Persona persona;
    private TipoServicio tipoServicio;
    private Contrato contrato;
    private Dependencia dependencia;
    private int estado;
    private Time hora;
    private Date fecha;

    private static final long serialVersionUID = 1L;
}
