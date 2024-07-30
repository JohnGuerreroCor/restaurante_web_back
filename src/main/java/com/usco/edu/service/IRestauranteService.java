package com.usco.edu.service;

import java.util.List;

import com.usco.edu.entities.RestauranteRaciones;
import com.usco.edu.entities.RestauranteSede;
import com.usco.edu.entities.RestauranteTiquetes;
import com.usco.edu.entities.RestauranteVenta;

public interface IRestauranteService {
	
	public List<RestauranteSede> obtenerSedesRestaurante();
	
	public List<RestauranteVenta> obtenerBoletos(int personaCodigo);
	
	public List<RestauranteRaciones> obtenerRacionesDisponibles(int restauranteSedeCodigo);
	
	public List<RestauranteTiquetes> obtenerTiquetesDisponibles(int restauranteSedeCodigo);

}
