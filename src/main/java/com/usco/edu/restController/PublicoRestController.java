package com.usco.edu.restController;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.dto.FotoAntigua;
import com.usco.edu.entities.Administrativo;
import com.usco.edu.entities.Docente;
import com.usco.edu.entities.Estudiante;
import com.usco.edu.entities.Firma;
import com.usco.edu.entities.Graduado;
import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.entities.PoliticaEstamento;
import com.usco.edu.entities.Ticket;
import com.usco.edu.service.IPublicoService;

@RestController
@RequestMapping(path = "publico")
public class PublicoRestController {
	
	@Autowired
	IPublicoService publicoService;
	
	@GetMapping(path = "estudiante-get/{codigo}")
	public List<Estudiante> buscarCodigoEstudiante(@PathVariable String codigo) {
		return publicoService.buscarCodigoEstudiante(codigo);
	}
	
	@GetMapping(path = "buscar-estudiante-identificacion/{id}")
	public List<Estudiante> buscarIdentificacionEstudiante(@PathVariable String id) {
		return publicoService.buscarIdentificacionEstudiante(id);
	}
	
	@GetMapping(path = "obtener-graduado/{id}")
	public List<Graduado> buscarIdentificacionGraduado(@PathVariable String id) {
		return publicoService.buscarIdentificacionGraduado(id);
	}
	
	@GetMapping(path = "administrativo-get/{id}")
	public List<Administrativo> buscarIdentificacionAdministrativo(@PathVariable String id) {
		return publicoService.buscarIdentificacionAdministrativo(id);
	}
	
	@GetMapping(path = "docente-get/{id}")
	public List<Docente> buscarIdentificacionDocente(@PathVariable String id) {
		return publicoService.buscarIdentificacionDocente(id);
	}
	
	@GetMapping("/obtener-persona-codigo/{codigo}")
	public List<PersonaCarnet> buscarCodigoPersona(@PathVariable("codigo") int codigo) {
		return publicoService.buscarCodigoPersona(codigo);
	}
	
	@GetMapping("/obtener-persona-identificacion/{id}")
	public List<PersonaCarnet> buscarIdentificacionPersona(@PathVariable("id") String id) {
		return publicoService.buscarIdentificacionPersona(id);
	}
	
	@GetMapping(path = "obtener-ticket-identificacion/{identificacion}")
	public List<Ticket> obtenerTicketIdentificacion(@PathVariable String identificacion) {
		return publicoService.obtenerTicketIdentificacion(identificacion);
	}
	
	@GetMapping(path = "obtener-firma-activa")
	public List<Firma> buscarFirmaActiva() {
		
		return publicoService.buscarFirmaActiva();
		
	}
	
	@GetMapping(path = "obtener-politicaPorCodigoEstamento/{codigo}")
	public List<PoliticaEstamento> obtenerPoliticaPorCodigoEstamento(@PathVariable int codigo) {
		
		return publicoService.obtenerPoliticaPorCodigoEstamento(codigo);
		
	}
	
	@GetMapping("mirar-archivo/{codigo}")
	public ResponseEntity<InputStreamResource> mirarArchivo(@PathVariable Long codigo, HttpServletResponse response) throws Exception{
		ByteArrayInputStream stream = publicoService.mirarArchivo(codigo, response);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=\" firma.png\"");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
		
	}
	
	
	@GetMapping("obtener-foto/{codigo}")
	public ResponseEntity<InputStreamResource> foto(HttpServletResponse response, @PathVariable String codigo) throws Exception{
		ByteArrayInputStream stream = publicoService.mirarFoto(codigo, response);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=\" foto.png\"");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(stream));
		
	}
	
	@GetMapping(path = "obtener-foto-antigua/{codigo}")
	public FotoAntigua fotoAntigua(@PathVariable String codigo) throws Exception{
		return publicoService.mirarFotoAntigua(codigo);
	}

}
