package com.usco.edu.service;

import java.util.List;

import com.usco.edu.entities.Venta;

public interface IVentaService {

	public List<Venta> obtenerVentasByPerCodigo(String userdb, int codigo, int codigoContrato);
	
	public int obtenerVentasDiariasOrdinarias(int tipoServicio, int codigoContrato);
	
	public int obtenerVentasDiariasGabus(int tipoServicio, int codigoContrato);

	public int registrarVentas(String userdb, List<Venta> ventas);

	public List<Long> cargarVentas(String userdb, List<Venta> ventas);

    public int actualizarVenta(String userdb, Venta venta);
    
    public int eliminarVenta(String userdb, Venta venta);
}
