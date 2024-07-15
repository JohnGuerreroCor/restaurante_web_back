package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.PersonaCarnet;

public interface IPersonaDao {
	
	public List<PersonaCarnet> buscarPorPerCodigo(String codigo);
	public List<PersonaCarnet> buscarPorIdentificacion(String id); 

}
