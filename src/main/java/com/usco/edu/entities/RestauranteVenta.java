package com.usco.edu.entities;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestauranteVenta implements Serializable {
	
	private String uaaRestauranteNombre;
	private int restauranteTipoServicioCodigo;
	private String restauranteTipoServicioNombre;
	private int restauranteBoleta;
	private Date restauranteContratoFecha;
	
	private static final long serialVersionUID = 1L;
	
}
