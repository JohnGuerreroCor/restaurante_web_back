package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.ReporteVenta;

public interface IReportesDao {
	
	public List<ReporteVenta> obtenerReporteVentas(int sede, String inicio, String fin);

}
