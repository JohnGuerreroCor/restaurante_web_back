package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IRestauranteDao;
import com.usco.edu.entities.RestauranteRaciones;
import com.usco.edu.entities.RestauranteSede;
import com.usco.edu.entities.RestauranteTiquetes;
import com.usco.edu.entities.RestauranteVenta;
import com.usco.edu.resultSetExtractor.RestauranteRacionesSetExtractor;
import com.usco.edu.resultSetExtractor.RestauranteSedeSetExtractor;
import com.usco.edu.resultSetExtractor.RestauranteTiquetesSetExtractor;
import com.usco.edu.resultSetExtractor.RestauranteVentaSetExtractor;

@Repository
public class RestauranteDaoImpl implements IRestauranteDao {
	
	
	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;
	
	@Override
	public List<RestauranteSede> obtenerSedesRestaurante() {
		
		String sql = "select * from sibusco.restaurante_sede rs ";
		return jdbcTemplate.query(sql, new RestauranteSedeSetExtractor());

	}

	@Override
	public List<RestauranteVenta> obtenerBoletos(int personaCodigo) {
		
		String sql = "DECLARE @CodigoContrato INT; "
				+ "DECLARE @CodigoSede INT; "
				+ "SET @CodigoSede = (SELECT rv.uaa_codigo FROM sibusco.restaurante_venta rv "
				+ "WHERE rv.per_codigo = " + personaCodigo + " AND rv.rve_estado = 1 "
				+ "group by rv.uaa_codigo); "
				+ "SET @CodigoContrato = (SELECT rco_codigo FROM sibusco.restaurante_contrato rc "
				+ "INNER JOIN sibusco.restaurante_tipo_contrato rtc ON rtc.rtc_codigo  = rc.rtc_codigo "
				+ "INNER JOIN dbo.uaa u ON u.uaa_codigo = rc.rco_uaa_codigo  "
				+ "WHERE rc.rco_uaa_codigo = @CodigoSede AND rc.rtc_codigo = 2 AND rc.rco_estado = 1 AND CONVERT(DATE, GETDATE()) BETWEEN rc.rco_fecha_inicial AND rc.rco_fecha_final); "
				+ "IF @CodigoContrato > 1 "
				+ "BEGIN "
				+ "select u.uaa_nombre, rts.rts_codigo, rts.rts_nombre, count(*) as cantidad, rc.rco_fecha_final from sibusco.restaurante_venta rv "
				+ "inner join sibusco.restaurante_tipo_servicio rts on rv.rts_codigo = rts.rts_codigo "
				+ "inner join sibusco.restaurante_pibote_adicion rpa on rv.rco_codigo = rpa.rco_codigo_general "
				+ "inner join sibusco.restaurante_contrato rc on rpa.rco_codigo_adicion =  rc.rco_codigo "
				+ "inner join dbo.uaa u on rv.uaa_codigo = u.uaa_codigo  "
				+ "where rv.per_codigo = " + personaCodigo + " and rv.rve_estado = 1 and rc.rco_estado = 1 and convert(Date, GETDATE()) between rc.rco_fecha_inicial  and rc.rco_fecha_final "
				+ "group by u.uaa_nombre,rts.rts_codigo, rts.rts_nombre, rc.rco_fecha_final "
				+ "END "
				+ "ELSE "
				+ "BEGIN "
				+ "select u.uaa_nombre, rts.rts_codigo, rts.rts_nombre, COUNT(*) as cantidad, rc.rco_fecha_final "
				+ "from sibusco.restaurante_venta rv "
				+ "inner join sibusco.restaurante_tipo_servicio rts on rv.rts_codigo = rts.rts_codigo "
				+ "inner join sibusco.restaurante_contrato rc on rv.rco_codigo =  rc.rco_codigo "
				+ "inner join dbo.uaa u on rv.uaa_codigo = u.uaa_codigo "
				+ "where rv.per_codigo = " + personaCodigo + " and rv.rve_estado = 1 and rc.rco_estado = 1 and convert(Date, GETDATE()) between rc.rco_fecha_inicial  and rc.rco_fecha_final "
				+ "group by u.uaa_nombre,rts.rts_codigo, rts.rts_nombre, rc.rco_fecha_final "
				+ "END";
		return jdbcTemplate.query(sql, new RestauranteVentaSetExtractor());
		
	}

	@Override
	public List<RestauranteRaciones> obtenerRacionesDisponibles(int restauranteSedeCodigo) {
		
		String sql = "WITH ConsumoPorRestaurante AS "
				+ "(SELECT rc.uaa_codigo, rc.rcn_fecha, rc.rts_codigo, COUNT(*) AS consumo FROM sibusco.restaurante_consumo rc "
				+ "left join sibusco.restaurante_grupo_gabu rgg on rc.per_codigo = rgg.per_codigo "
				+ "WHERE rc.uaa_codigo = " + restauranteSedeCodigo + " and rc.rcn_fecha = CONVERT(DATE, GETDATE()) and rgg.per_codigo IS NULL "
				+ "GROUP BY rc.uaa_codigo, rc.rcn_fecha, rc.rts_codigo) "
				+ "SELECT rhs.rts_codigo, rts.rts_nombre, rhs.rhs_uaa_codigo, rs.uaa_nombre, rhs.rhs_cantidad_comidas, "
				+ "consumo, rhs.rhs_cantidad_comidas - c.consumo AS raciones_disponibles, rhs.rhs_hora_inicio_atencion, "
				+ "rhs.rhs_hora_fin_atencion FROM sibusco.restaurante_horario_servicio rhs "
				+ "INNER JOIN ConsumoPorRestaurante c ON rhs.rhs_uaa_codigo = c.uaa_codigo AND rhs.rts_codigo = c.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_servicio rts on rhs.rts_codigo = rts.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_sede rs on rhs.rhs_uaa_codigo = rs.uaa_codigo "
				+ "WHERE CONVERT(TIME, GETDATE()) BETWEEN rhs.rhs_hora_inicio_atencion AND rhs.rhs_hora_fin_atencion";
		
		return jdbcTemplate.query(sql, new RestauranteRacionesSetExtractor());
		
	}

	@Override
	public List<RestauranteTiquetes> obtenerTiquetesDisponibles(int restauranteSedeCodigo) {
		
		String sql = "WITH VentaPorRestaurante AS "
				+ "(SELECT rhs.rhs_uaa_codigo, rv.rts_codigo, COUNT(rv.rve_codigo) AS ventas FROM sibusco.restaurante_horario_servicio rhs "
				+ "INNER JOIN sibusco.restaurante_venta rv ON rhs.rhs_uaa_codigo = rv.uaa_codigo and rv.rve_fecha = CONVERT(DATE, GETDATE()) "
				+ "left join sibusco.restaurante_grupo_gabu rgg on rv.per_codigo = rgg.per_codigo "
				+ "WHERE rhs.rhs_uaa_codigo = " + restauranteSedeCodigo + " AND (CONVERT(TIME, GETDATE()) BETWEEN rhs.rhs_hora_inicio_venta AND rhs.rhs_hora_fin_atencion and rv.rve_eliminado != 0 AND rgg.per_codigo IS NULL) "
				+ "GROUP BY rhs.rhs_uaa_codigo, rv.rts_codigo) "
				+ "SELECT rhs.rts_codigo, rts.rts_nombre, rhs.rhs_uaa_codigo, rs.uaa_nombre, rhs.rhs_cantidad_ventas_permitidas, "
				+ "ventas, rhs.rhs_cantidad_ventas_permitidas - v.ventas AS tiquetes_disponibles, rhs.rhs_hora_inicio_venta, "
				+ "rhs.rhs_hora_fin_venta FROM sibusco.restaurante_horario_servicio rhs "
				+ "INNER JOIN VentaPorRestaurante v ON rhs.rhs_uaa_codigo = v.rhs_uaa_codigo AND rhs.rts_codigo = v.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_servicio rts on rhs.rts_codigo = rts.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_sede rs on rhs.rhs_uaa_codigo = rs.uaa_codigo "
				+ "WHERE CONVERT(TIME, GETDATE()) BETWEEN rhs.rhs_hora_inicio_atencion AND rhs.rhs_hora_fin_atencion;";
		
		return jdbcTemplate.query(sql, new RestauranteTiquetesSetExtractor());
		
	}
	
}
