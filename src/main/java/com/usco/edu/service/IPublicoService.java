package com.usco.edu.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.usco.edu.dto.FotoAntigua;
import com.usco.edu.entities.Administrativo;
import com.usco.edu.entities.Docente;
import com.usco.edu.entities.Estudiante;
import com.usco.edu.entities.Firma;
import com.usco.edu.entities.Graduado;
import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.entities.PoliticaEstamento;
import com.usco.edu.entities.Ticket;

public interface IPublicoService {
	
public List<Estudiante> buscarCodigoEstudiante(String codigo);
	
	public List<Estudiante> buscarIdentificacionEstudiante(String id);
	
	public List<Graduado> buscarIdentificacionGraduado( String id);
	
	public List<Administrativo> buscarIdentificacionAdministrativo(String id);
	
	public List<Docente> buscarIdentificacionDocente( String id);
	
	public List<PersonaCarnet> buscarCodigoPersona(int codigo);
	
	public List<PersonaCarnet> buscarIdentificacionPersona(String id); 
	
	public List<Ticket> obtenerTicketIdentificacion(String identificacion);
	
	public List<Firma> buscarFirmaActiva();
	
	public List<PoliticaEstamento> obtenerPoliticaPorCodigoEstamento(int codigo);
	
	ByteArrayInputStream mirarFoto(String codigo, HttpServletResponse response);
	
	ByteArrayInputStream mirarArchivo(long archivoCodigo, HttpServletResponse response);
	
	FotoAntigua mirarFotoAntigua(String codigo);

}
