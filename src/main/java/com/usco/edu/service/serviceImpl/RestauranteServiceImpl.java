package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IRestauranteDao;
import com.usco.edu.entities.RestauranteRaciones;
import com.usco.edu.entities.RestauranteSede;
import com.usco.edu.entities.RestauranteTiquetes;
import com.usco.edu.entities.RestauranteVenta;
import com.usco.edu.service.IRestauranteService;

@Service
public class RestauranteServiceImpl implements IRestauranteService {
	
	@Autowired
	private IRestauranteDao restauranteDao;
	
	@Override
	public List<RestauranteSede> obtenerSedesRestaurante() {
		
		return restauranteDao.obtenerSedesRestaurante();
		
	}

	@Override
	public List<RestauranteVenta> obtenerBoletos(int personaCodigo) {
		
		return restauranteDao.obtenerBoletos(personaCodigo);
		
	}

	@Override
	public List<RestauranteRaciones> obtenerRacionesDisponibles(int restauranteSedeCodigo) {
		
		return restauranteDao.obtenerRacionesDisponibles(restauranteSedeCodigo);
		
	}

	@Override
	public List<RestauranteTiquetes> obtenerTiquetesDisponibles(int restauranteSedeCodigo) {
		
		return restauranteDao.obtenerTiquetesDisponibles(restauranteSedeCodigo);
		
	}
	
}