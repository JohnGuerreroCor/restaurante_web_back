package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.WebParametro;

public interface IWebParametroDao {
	
	public List<WebParametro> obtenerWebParametro();
	
	public int actualizarSemilla(byte[] seed);
	
	public byte[] obtenerSemilla();

}
