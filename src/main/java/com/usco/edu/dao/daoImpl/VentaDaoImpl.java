package com.usco.edu.dao.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


import com.usco.edu.dao.IVentaDao;
import com.usco.edu.entities.Venta;
import com.usco.edu.resultSetExtractor.VentaSetExtractor;
import com.usco.edu.util.AuditoriaJdbcTemplate;

@Repository
public class VentaDaoImpl implements IVentaDao {

	@Autowired
	private AuditoriaJdbcTemplate jdbcComponent;

	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("JDBCTemplateEjecucion")
	public JdbcTemplate jdbcTemplateEjecucion;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc; // Add this line to autowire NamedParameterJdbcTemplate

	@Autowired
	private DataSource dataSource; // Add this line to autowire DataSource

	@Override
	public List<Venta> obtenerVentasByPerCodigo(String userdb, int codigoPersona, int codigoContrato) {
		String sql = "SELECT * FROM sibusco.restaurante_venta rev "
				+ "INNER JOIN dbo.persona p ON p.per_codigo = rev.per_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_servicio rts ON rts.rts_codigo = rev.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_contrato rc ON rc.rco_codigo = rev.rco_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_contrato rtc ON rtc.rtc_codigo = rc.rtc_codigo "
				+ "INNER JOIN dbo.uaa u ON u.uaa_codigo = rev.uaa_codigo " + "WHERE rev.per_codigo = " + codigoPersona
				+ " " + "AND rev.rco_codigo = " + codigoContrato + " AND rev.rve_estado = 1 " + " AND rev.rve_eliminado = 1 "
				+ "ORDER BY rev.rve_fecha DESC";
		return jdbcTemplate.query(sql, new VentaSetExtractor());
	}
	
	@Override
	public int obtenerVentasDiariasOrdinarias(int tipoServicio, int codigoContrato) {
		String sql = "SELECT COUNT(*) AS cantidad_registros FROM sibusco.restaurante_venta rv "
		        + "	LEFT JOIN sibusco.restaurante_grupo_gabu rgg ON rv.per_codigo = rgg.per_codigo "
		        + "	WHERE rv.rts_codigo = " + tipoServicio
		        + " AND rv.rve_eliminado = 1 "
		        + " AND rv.rco_codigo = " + codigoContrato
		        + " AND rv.rve_fecha = CONVERT(DATE, GETDATE()) "
		        + " AND rgg.per_codigo IS NULL";

		
		int cantidadRegistros = jdbcTemplate.queryForObject(sql, Integer.class);
		
		return cantidadRegistros;
	}
	
	@Override
	public int obtenerVentasDiariasGabus(int tipoServicio, int codigoContrato) {
		String sql = "SELECT COUNT(*) AS cantidad_registros FROM sibusco.restaurante_venta rv "
				+ " INNER JOIN sibusco.restaurante_grupo_gabu rgg ON rv.per_codigo = rgg.per_codigo "
				+ " WHERE rv.rts_codigo = " + tipoServicio
				+ " AND rv.rve_eliminado = 1 "
				+ " AND rv.rco_codigo = " + codigoContrato + " "
				+ " AND rv.rve_fecha = CONVERT(DATE, GETDATE())";
		
		int cantidadRegistros = jdbcTemplate.queryForObject(sql, Integer.class);
		
		return cantidadRegistros;
	}

	
	@Override
	public int registrarVentas(String userdb, List<Venta> ventas) {
	    // Evitar la concatenación directa de valores en el SQL para prevenir inyección SQL
	    String sql = "DECLARE @cantidad_tiquetes INT; "
	            + "SELECT @cantidad_tiquetes = rhs_cantidad_tiquetes "
	            + "FROM sibusco.restaurante_horario_servicio rhs "
	            + "WHERE rhs.rhs_uaa_codigo = ? AND rhs.rts_codigo = ?; "
	            + "IF ( "
	            + "    SELECT COUNT(*) AS cantidad_registros "
	            + "    FROM sibusco.restaurante_venta rv "
	            + "    WHERE rv.per_codigo = ? "
	            + "    AND rv.rts_codigo = ? "
	            + "    AND rv.rve_estado = ? "
	            + ") < @cantidad_tiquetes "
	            + "BEGIN "
	            + "    INSERT INTO sibusco.restaurante_venta "
	            + "        (per_codigo, rts_codigo, rco_codigo, uaa_codigo, rve_estado, rve_fecha, rve_hora, rve_eliminado) "
	            + "    VALUES "
	            + "        (?, ?, ?, ?, ?, CONVERT(DATE, GETDATE()), CONVERT(TIME, GETDATE()), ?); "
	            + "    SELECT 1; "
	            + "END "
	            + "ELSE "
	            + "BEGIN "
	            + "    SELECT 0; "
	            + "END";

	    int totalInserted = 0; // Inicializa el contador para el recuento total de inserciones

	    try {
	        // Utiliza PreparedStatement para evitar inyección SQL
	        PreparedStatement preparedStatement = jdbcTemplateEjecucion.getDataSource().getConnection().prepareStatement(sql);
	        for (Venta venta : ventas) {
	            // Establece los parámetros en el PreparedStatement
	            preparedStatement.setInt(1, venta.getDependencia().getCodigo());
	            preparedStatement.setInt(2, venta.getTipoServicio().getCodigo());
	            preparedStatement.setLong(3, venta.getPersona().getCodigo());
	            preparedStatement.setInt(4, venta.getTipoServicio().getCodigo());
	            preparedStatement.setInt(5, venta.getEstado());
	            preparedStatement.setLong(6, venta.getPersona().getCodigo());
	            preparedStatement.setInt(7, venta.getTipoServicio().getCodigo());
	            preparedStatement.setInt(8, venta.getContrato().getCodigo());
	            preparedStatement.setInt(9, venta.getDependencia().getCodigo());
	            preparedStatement.setInt(10, venta.getEstado());
	            preparedStatement.setInt(11, venta.getEliminado());

	            // Ejecuta la consulta preparada
	            int result = preparedStatement.executeUpdate();
	            totalInserted += result; // Agrega el resultado de la inserción al contador total
	        }
	    } catch (SQLException e) {
	        // Maneja las excepciones adecuadamente
	        e.printStackTrace();
	        return 0;
	    }

	    // Retorna el recuento total de inserciones exitosas
	    return totalInserted;
	}



	@Override
	public int actualizarVenta(String userdb, Venta venta) {

		String sql = "UPDATE sibusco.restaurante_venta " + "SET rve_estado=? " + "WHERE rve_codigo=?";

		try {

			int result = jdbcTemplateEjecucion.update(sql, new Object[] { venta.getEstado(), venta.getCodigo(), });

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int eliminarVenta(String userdb, Venta venta) {

		String sql = "UPDATE sibusco.restaurante_venta " + "SET rve_estado=?, rve_eliminado=? " + "WHERE rve_codigo=?";

		try {

			int result = jdbcTemplateEjecucion.update(sql, new Object[] { venta.getEstado(), venta.getEliminado(), venta.getCodigo(), });

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void cerrarConexion(Connection con) {
		if (con == null)
			return;

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Long> cargarVentas(String userdb, List<Venta> ventas) {
	    String sql = "DECLARE @perCodigo INT; "
	            + "SET @perCodigo = (SELECT e.per_codigo FROM estudiante e WHERE e.est_codigo = :estudianteCodigo); "
	            + "IF @perCodigo IS NULL "
	            + "BEGIN "
	            + "    SET @perCodigo = (SELECT p.per_codigo FROM persona p WHERE p.per_identificacion = :id); "
	            + "    IF @perCodigo IS NOT NULL "
	            + "    BEGIN "
	            + "        INSERT INTO sibusco.restaurante_venta "
	            + "            (per_codigo, rts_codigo, rco_codigo, uaa_codigo, rve_estado, rve_fecha, rve_hora) "
	            + "        VALUES "
	            + "            (@perCodigo, :tipoServicio, :contrato, :uaa, :estado, :fecha, :hora); "
	            + "    END "
	            + "END "
	            + "ELSE "
	            + "    INSERT INTO sibusco.restaurante_venta "
	            + "        (per_codigo, rts_codigo, rco_codigo, uaa_codigo, rve_estado, rve_fecha, rve_hora) "
	            + "    VALUES "
	            + "        (@perCodigo, :tipoServicio, :contrato, :uaa, :estado, :fecha, :hora);";

	    try {
	        SqlParameterSource[] batchParams = ventas.stream()
	                .map(venta -> new MapSqlParameterSource()
	                        .addValue("estudianteCodigo", venta.getPersona().getIdentificacion())
	                        .addValue("id", venta.getPersona().getIdentificacion())
	                        .addValue("tipoServicio", venta.getTipoServicio().getCodigo())
	                        .addValue("contrato", venta.getContrato().getCodigo())
	                        .addValue("uaa", venta.getDependencia().getCodigo())
	                        .addValue("estado", venta.getEstado())
	                        .addValue("fecha", venta.getFecha())
	                        .addValue("hora", venta.getHora())
	                		)
	                .toArray(SqlParameterSource[]::new);

	        int[] updateCounts = jdbc.batchUpdate(sql, batchParams);

	        // Contar inserciones insatisfactorias
	        int totalUnsuccessful = 0;
	        int totalSuccessful = 0;
	        List<Long> registrosErrados = new ArrayList<>();
	        
	        for (int i = 0; i < updateCounts.length; i++) {
	        	int count = updateCounts[i];
	        	
	            if (count < 0) {
	                totalUnsuccessful++;
	                registrosErrados.add(Long.parseLong(ventas.get(Math.abs(i)).getPersona().getIdentificacion()));
	            } else {
	            	totalSuccessful++;
	            }
	        }
	        
	        System.out.println(totalUnsuccessful);
	        System.out.println(totalSuccessful);

	        return registrosErrados;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; // Devuelve el número total de ventas si ocurre una excepción
	    } finally {
	        try {
	            cerrarConexion(dataSource.getConnection());
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


}
