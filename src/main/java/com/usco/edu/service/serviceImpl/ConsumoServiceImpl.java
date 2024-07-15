package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IConsumoDao;
import com.usco.edu.entities.Consumo;
import com.usco.edu.service.IConsumoService;

@Service
public class ConsumoServiceImpl implements IConsumoService {

	@Autowired
	private IConsumoDao ConsumoDao;

	@Override
	public List<Consumo> obtenerConsumoByPerCodigo(String userdb, int codigoPersona, int codigoContrato) {
		return ConsumoDao.obtenerConsumoByPerCodigo(userdb, codigoPersona, codigoContrato);
	}
	
	@Override
	public int obtenerConsumosDiarios(int tipoServicio, int codigoContrato) {
		return ConsumoDao.obtenerConsumosDiarios(tipoServicio, codigoContrato);
	}

	@Override
	public int registrarConsumo(String userdb, Consumo consumo, int percodigo, int tipoServicio) {
		return ConsumoDao.registrarConsumo(userdb, consumo, percodigo, tipoServicio);
	}

	@Override
	public int actualizarConsumo(String userdb, Consumo consumo) {
		return ConsumoDao.actualizarConsumo(userdb, consumo);
	}

	@Override
	public List<Long> cargarConsumos(String userdb, List<Consumo> consumos) {
		return ConsumoDao.cargarConsumos(userdb, consumos);
	}
	
}
