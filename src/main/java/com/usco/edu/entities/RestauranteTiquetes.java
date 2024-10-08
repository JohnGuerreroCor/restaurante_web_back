package com.usco.edu.entities;

import java.io.Serializable;
import java.sql.Time;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestauranteTiquetes implements Serializable {
	
	private int restauranteTipoServicioCodigo;
	private String restauranteTipoServicioNombre;
	private int uaaRestauranteCodigo;
	private String uaaRestauranteNombre;
	private Time restauranteHoraServicioInicio;
	private Time restauranteHoraServicioFin;
	private int restauranteCantidadTiquetes;
	private int restauranteCantidadVentas;
	private int restauranteCantidaTiquetesDisponibles;
	
	private static final long serialVersionUID = 1L;

}
