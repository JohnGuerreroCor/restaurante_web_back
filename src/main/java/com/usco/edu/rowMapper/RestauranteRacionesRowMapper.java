package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.RestauranteRaciones;

public class RestauranteRacionesRowMapper implements RowMapper<RestauranteRaciones>{

	@Override
	public RestauranteRaciones mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestauranteRaciones restauranteRaciones = new RestauranteRaciones();
		restauranteRaciones.setRestauranteTipoServicioCodigo(rs.getInt("rts_codigo"));
		restauranteRaciones.setRestauranteTipoServicioNombre(rs.getString("rts_nombre"));
		restauranteRaciones.setUaaRestauranteCodigo(rs.getInt("rhs_uaa_codigo"));
		restauranteRaciones.setUaaRestauranteNombre(rs.getString("uaa_nombre"));
		restauranteRaciones.setRestauranteHoraServicioInicio(rs.getTime("rhs_hora_inicio_atencion"));
		restauranteRaciones.setRestauranteHoraServicioFin(rs.getTime("rhs_hora_fin_atencion"));
		restauranteRaciones.setRestauranteCantidadRaciones(rs.getInt("rhs_cantidad_comidas"));
		restauranteRaciones.setRestauranteCantidadVentas(rs.getInt("consumo"));
		restauranteRaciones.setRestauranteCantidaRacionesDisponibles(rs.getInt("raciones_disponibles"));
		return restauranteRaciones;
	}
}
