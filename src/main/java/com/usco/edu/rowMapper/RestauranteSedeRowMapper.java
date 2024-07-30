package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.RestauranteSede;

public class RestauranteSedeRowMapper implements RowMapper<RestauranteSede>{

	@Override
	public RestauranteSede mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestauranteSede restauranteSede = new RestauranteSede();
		restauranteSede.setUaaCodigo(rs.getInt("uaa_codigo"));
		restauranteSede.setUaaNombre(rs.getString("uaa_nombre"));
		restauranteSede.setSedeCodigo(rs.getInt("sed_codigo"));
		restauranteSede.setSedeNombre(rs.getString("sed_nombre"));
		return restauranteSede;
	}
}
