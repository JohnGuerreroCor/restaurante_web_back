package com.usco.edu.entities;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReporteVenta {
	
	private Integer codigo;
	private Integer personaCodigo;
	private String personaIdentificacion;
	private String personaNombre;
	private String gabu;
	private Integer tipoServicioCodigo;
	private String tipoServicioNombre;
	private Integer contratoCodigo;
	private Integer contratoTipoCodigo;
	private String contratoTipoNombre;
	private Integer sedeCodigo;
	private String sedeNombre;
	private Integer estado;
	private Date fecha;
	private Time hora;
	private Integer eliminado;

}