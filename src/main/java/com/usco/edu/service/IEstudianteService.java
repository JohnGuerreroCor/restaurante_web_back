package com.usco.edu.service;
import java.util.List;

import com.usco.edu.entities.Estudiante;

public interface IEstudianteService {
	
	public List<Estudiante> findByCodigo(String codigo);
	
	public List<Estudiante> buscarIdentificacion(String id);

	public List<Estudiante> buscarPerCodigo(int perCodigo);
	
}
