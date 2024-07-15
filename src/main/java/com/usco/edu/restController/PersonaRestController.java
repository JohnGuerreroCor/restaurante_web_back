package com.usco.edu.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.service.IPersonaService;

@RestController
@RequestMapping(path = "persona")
public class PersonaRestController {
	
	@Autowired
	IPersonaService personaService;
	
	@GetMapping("/obtener-persona-codigo/{codigo}")
	public List<PersonaCarnet> buscarPorPerCodigo(@PathVariable("codigo") String codigo) {
		return personaService.buscarPorPerCodigo(codigo);
	}
	
	@GetMapping("/obtener-persona-identificacion/{id}")
	public List<PersonaCarnet> buscarPorIdentificacion(@PathVariable("id") String id) {
		return personaService.buscarPorIdentificacion(id);
	}

}
