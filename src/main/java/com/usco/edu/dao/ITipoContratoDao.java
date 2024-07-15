package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.TipoContrato;

public interface ITipoContratoDao {

	public List<TipoContrato> obtenerTiposContrato(String userdb);

	int actualizarTipoContrato(String userdb, TipoContrato tipoContrato);

	int crearTipoContrato(String userdb, TipoContrato tipoContrato);

}
