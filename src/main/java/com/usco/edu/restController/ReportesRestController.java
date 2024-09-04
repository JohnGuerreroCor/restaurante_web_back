package com.usco.edu.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.ReporteVenta;
import com.usco.edu.service.IReportesService;

@RestController
@RequestMapping(path = "reportes")
public class ReportesRestController {
	
	@Autowired
	private IReportesService reportesService;
	
	@GetMapping(path = "obtener-reporte-ventas/{sede}/{inicio}/{fin}")
	public List<ReporteVenta> obtenerReporteVentas(@PathVariable("sede") int sede, @PathVariable("inicio") String inicio, @PathVariable("fin") String fin) {
		
		return reportesService.obtenerReporteVentas(sede, inicio, fin);
		
	}

}
