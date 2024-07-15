package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IContratoDao;
import com.usco.edu.entities.Contrato;
import com.usco.edu.service.IContratoService;

@Service
public class ContratoServiceImpl implements IContratoService {

	@Autowired
	private IContratoDao ContratoDao;

	@Override
	public List<Contrato> obtenerContratos(String userdb) {

		return ContratoDao.obtenerContratos(userdb);
	}

	@Override
	public List<Contrato> obtenerContrato(String userdb, int codigo) {

		return ContratoDao.obtenerContrato(userdb, codigo);
	}

	@Override
	public int crearContrato(String userdb, Contrato contrato) {

		return ContratoDao.crearContrato(userdb, contrato);
	}

	@Override
	public int actualizarContrato(String userdb, Contrato contrato) {

		return ContratoDao.actualizarContrato(userdb, contrato);
	}

	@Override
	public List<Contrato> obtenerContratosByVigencia(String userdb, int codigoUaa, String fecha) {

		return ContratoDao.obtenerContratosByVigencia(userdb, codigoUaa, fecha);
	}

}
