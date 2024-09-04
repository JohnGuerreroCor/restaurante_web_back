package com.usco.edu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IReportesDao;
import com.usco.edu.entities.ReporteVenta;
import com.usco.edu.service.IReportesService;

@Service
public class ReportesServiceImpl implements IReportesService {

	@Autowired
	private IReportesDao reportesDao;

	@Override
	public List<ReporteVenta> obtenerReporteVentas(int sede, String inicio, String fin) {
		
		return reportesDao.obtenerReporteVentas(sede, inicio, fin);
		
	}
	
}
