package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IPersonaDao;
import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.service.IPersonaService;


@Service
public class PersonaServiceImpl implements IPersonaService{
	
	@Autowired
	private IPersonaDao personaDao;


	@Override
	public List<PersonaCarnet> buscarPorPerCodigo(String codigo) {
		return personaDao.buscarPorPerCodigo(codigo);
	}
	
	@Override
	public List<PersonaCarnet> buscarPorIdentificacion(String id) {
		return personaDao.buscarPorIdentificacion(id);
	}

}
