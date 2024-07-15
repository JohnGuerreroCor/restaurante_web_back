package com.usco.edu.service;

import java.util.List;

import com.usco.edu.entities.WebParametro;

public interface IWebParametroService {
	
	public List<WebParametro> obtenerWebParametro();
	
	public int actualizarSemilla(byte[] seed);
	
	public byte[] obtenerSemilla();

}
