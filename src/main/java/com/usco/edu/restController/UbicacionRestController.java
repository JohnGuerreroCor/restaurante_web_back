package com.usco.edu.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.SedeCarnet;
import com.usco.edu.entities.SubSede;
import com.usco.edu.service.IUbicacionService;

@RestController
@RequestMapping(path = "ubicacion")
public class UbicacionRestController {
	
	@Autowired
	IUbicacionService ubicacionService;
	
	@GetMapping(path = "obtener-sedes/{username}")
	public List<SedeCarnet> obtenerSedes(@PathVariable String username) {
		
		return ubicacionService.obtenerSedes(username);
		
	}
	
	@GetMapping(path = "obtener-subsedes/{username}")
	public List<SubSede> obtenSubSedes(@PathVariable String username) {
		
		return ubicacionService.obtenerSubSedes(username);
		
	}
	
	@GetMapping(path = "buscar-subsede/{codigo}/{username}")
	public List<SubSede> buscarSubSedePorSede(@PathVariable int codigo, @PathVariable String username) {
		
		return ubicacionService.buscarSubSedePorSede(codigo, username);
		
	}

}
