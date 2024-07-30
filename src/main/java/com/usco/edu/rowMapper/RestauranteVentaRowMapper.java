package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.RestauranteVenta;

public class RestauranteVentaRowMapper implements RowMapper<RestauranteVenta>{

	@Override
	public RestauranteVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestauranteVenta restauranteVenta = new RestauranteVenta();
		restauranteVenta.setUaaRestauranteNombre(rs.getString("uaa_nombre"));
		restauranteVenta.setRestauranteTipoServicioCodigo(rs.getInt("rts_codigo"));
		restauranteVenta.setRestauranteTipoServicioNombre(rs.getString("rts_nombre"));
		restauranteVenta.setRestauranteBoleta(rs.getInt("cantidad"));
		restauranteVenta.setRestauranteContratoFecha(rs.getDate("rco_fecha_final"));
		return restauranteVenta;
	}
}
