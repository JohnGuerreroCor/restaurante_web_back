package com.usco.edu.service;

import java.util.List;

import com.usco.edu.entities.SedeCarnet;
import com.usco.edu.entities.SubSede;

public interface IUbicacionService {
	
	//LISTAR TODAS LAS CATEGORIAS
	
		public List<SedeCarnet> obtenerSedes(String userdb);
		
		public List<SubSede> obtenerSubSedes(String userdb);
		
		
		//BUSCAR POR CATEGORIA PADRE A HIJA
		
		public List<SubSede> buscarSubSedePorSede(int codigo, String userdb);
		

}
