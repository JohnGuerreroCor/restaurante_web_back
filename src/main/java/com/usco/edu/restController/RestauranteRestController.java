package com.usco.edu.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.RestauranteRaciones;
import com.usco.edu.entities.RestauranteSede;
import com.usco.edu.entities.RestauranteTiquetes;
import com.usco.edu.entities.RestauranteVenta;
import com.usco.edu.service.IRestauranteService;

@RestController
@RequestMapping(path = "restaurante")
public class RestauranteRestController {
	
	@Autowired
	private IRestauranteService restauranteService;
	
	@GetMapping("obtener-sedes-restaurante")
	public List<RestauranteSede> obtenerSedesRestaurante() {
		return restauranteService.obtenerSedesRestaurante();
	}

	@GetMapping("obtener-boletas/{codigo}")
	public List<RestauranteVenta> obtenerBoletos(@PathVariable int codigo) {
		return restauranteService.obtenerBoletos(codigo);
	}
	
	@GetMapping("obtener-raciones-disponibles/{codigo}")
	public List<RestauranteRaciones> obtenerRacionesDisponibles(@PathVariable int codigo) {
		return restauranteService.obtenerRacionesDisponibles(codigo);
	}
	
	@GetMapping("obtener-tiquetes-disponibles/{codigo}")
	public List<RestauranteTiquetes> obtenerTiquetesDisponibles(@PathVariable int codigo) {
		return restauranteService.obtenerTiquetesDisponibles(codigo);
	}

}
