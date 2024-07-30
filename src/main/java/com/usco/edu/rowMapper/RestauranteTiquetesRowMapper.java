package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.RestauranteTiquetes;

public class RestauranteTiquetesRowMapper implements RowMapper<RestauranteTiquetes>{

	@Override
	public RestauranteTiquetes mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestauranteTiquetes restauranteTiquetes = new RestauranteTiquetes();
		restauranteTiquetes.setRestauranteTipoServicioCodigo(rs.getInt("rts_codigo"));
		restauranteTiquetes.setRestauranteTipoServicioNombre(rs.getString("rts_nombre"));
		restauranteTiquetes.setUaaRestauranteCodigo(rs.getInt("rhs_uaa_codigo"));
		restauranteTiquetes.setUaaRestauranteNombre(rs.getString("uaa_nombre"));
		restauranteTiquetes.setRestauranteHoraServicioInicio(rs.getTime("rhs_hora_inicio_venta"));
		restauranteTiquetes.setRestauranteHoraServicioFin(rs.getTime("rhs_hora_fin_venta"));
		restauranteTiquetes.setRestauranteCantidadTiquetes(rs.getInt("rhs_cantidad_ventas_permitidas"));
		restauranteTiquetes.setRestauranteCantidadVentas(rs.getInt("ventas"));
		restauranteTiquetes.setRestauranteCantidaTiquetesDisponibles(rs.getInt("tiquetes_disponibles"));
		return restauranteTiquetes;
	}
}
