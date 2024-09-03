package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IGrupoGabuDao;
import com.usco.edu.entities.GrupoGabu;
import com.usco.edu.service.IGrupoGabuService;

@Service
public class GrupoGabuServiceImpl implements IGrupoGabuService {

	@Autowired
	private IGrupoGabuDao grupoGabuDao;

	@Override
	public List<GrupoGabu> obtenerGrupoGabus(String userdb) {
		return grupoGabuDao.obtenerGrupoGabus(userdb);
	}

	@Override
	public List<GrupoGabu> obtenerGrupoGabu(String userdb, int codigo) {
		return grupoGabuDao.obtenerGrupoGabu(userdb, codigo);
	}

	@Override
	public int crearGrupoGabu(String userdb, GrupoGabu grupoGabu) {
		return grupoGabuDao.crearGrupoGabu(userdb, grupoGabu);
	}

	@Override
	public int actualizarGrupoGabu(String userdb, GrupoGabu grupoGabu) {
		return grupoGabuDao.actualizarGrupoGabu(userdb, grupoGabu);
	}

}
