package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IWebParametroDao;
import com.usco.edu.entities.WebParametro;
import com.usco.edu.service.IWebParametroService;

@Service
public class WebParametroServiceImpl implements IWebParametroService {

	@Autowired
	private IWebParametroDao webParametroDao;

	@Override
	public List<WebParametro> obtenerWebParametro() {

		return webParametroDao.obtenerWebParametro();

	}

	@Override
	public int actualizarSemilla(byte[] seed) {

		return webParametroDao.actualizarSemilla(seed);
	}

	@Override
	public byte[] obtenerSemilla() {
	
		return webParametroDao.obtenerSemilla();
	}

}
