package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.ITipoServicioDao;
import com.usco.edu.entities.TipoServicio;
import com.usco.edu.service.ITipoServicioService;

@Service
public class TipoServicioServiceImpl implements ITipoServicioService {

	@Autowired
	private ITipoServicioDao tipoServicioDao;

	@Override
	public List<TipoServicio> obtenerTiposServicio(String userdb) {
		return tipoServicioDao.obtenerTiposServicio(userdb);
	}

	@Override
	public int actualizarTipoServicio(String userdb, TipoServicio tipoServicio) {
		return tipoServicioDao.actualizarTipoServicio(userdb, tipoServicio);
	}

	@Override
	public int crearTipoServicio(String userdb, TipoServicio tipoServicio) {
		return tipoServicioDao.crearTipoServicio(userdb, tipoServicio);
	}
}
