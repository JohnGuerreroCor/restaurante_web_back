package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.Venta;

public interface IVentaDao {

    public List<Venta> obtenerVentasByPerCodigo(String userdb, int codigoPersona, int codigoContrato);
    
    public int obtenerVentasDiarias(int tipoServicio, int codigoContrato);

    public int registrarVentas(String userdb, List<Venta> ventas);
    
    public List<Long> cargarVentas(String userdb, List<Venta> ventas);

    public int actualizarVenta(String userdb, Venta venta);
}
