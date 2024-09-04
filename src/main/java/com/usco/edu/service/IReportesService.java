package com.usco.edu.service;

import java.util.List;

import com.usco.edu.entities.ReporteVenta;

public interface IReportesService {
	
	public List<ReporteVenta> obtenerReporteVentas(int sede, String inicio, String fin);

}
