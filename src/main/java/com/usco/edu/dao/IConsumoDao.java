package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.Consumo;

public interface IConsumoDao {

	public List<Consumo> obtenerConsumoByPerCodigo(String userdb, int codigoPersona, int codigoContrato);
	
	public int obtenerConsumosDiarios(int tipoServicio, int codigoContrato);

	public int registrarConsumo(String userdb, Consumo consumo, int percodigo, int tipoServicio);
	
	public List<Long> cargarConsumos(String userdb, List<Consumo> consumos);

	public int actualizarConsumo(String userdb, Consumo consumo);
}
