package com.usco.edu.dao.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IConsumoDao;
import com.usco.edu.entities.Consumo;
import com.usco.edu.resultSetExtractor.ConsumoSetExtractor;
import com.usco.edu.util.AuditoriaJdbcTemplate;

@Repository
public class ConsumoDaoImpl implements IConsumoDao {

	@Autowired
	private AuditoriaJdbcTemplate jdbcComponent;

	@Autowired
	@Qualifier("JDBCTemplatePlanesConsulta")
	public JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("JDBCTemplateEjecucion")
	public JdbcTemplate jdbcTemplateEjecucion;
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc; // Add this line to autowire NamedParameterJdbcTemplate

	@Autowired
	private DataSource dataSource; // Add this line to autowire DataSource

	@Override
	public List<Consumo> obtenerConsumoByPerCodigo(String userdb, int codigoPersona, int codigoContrato) {
		String sql = "SELECT * FROM "
				+ "sibusco.restaurante_consumo rcn "
				+ "INNER JOIN dbo.persona p ON "
				+ "	p.per_codigo = rcn.per_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_servicio rts ON "
				+ "	rts.rts_codigo = rcn.rts_codigo "
				+ "INNER JOIN sibusco.restaurante_venta rv ON "
				+ "	rv.rve_codigo = rcn.rve_codigo "
				+ "INNER JOIN sibusco.restaurante_contrato rc ON "
				+ "	rc.rco_codigo = rcn.rco_codigo "
				+ "INNER JOIN sibusco.restaurante_tipo_contrato rtc ON "
				+ "	rtc.rtc_codigo = rc.rtc_codigo "
				+ "INNER JOIN dbo.uaa u ON "
				+ "	u.uaa_codigo = rcn.uaa_codigo "
				+ "WHERE "
				+ "	rcn.per_codigo = " + codigoPersona + " "
				+ "	AND rcn.rco_codigo = " + codigoContrato + " "
				+ "	AND rcn.rcn_estado = 1;";
		return jdbcTemplate.query(sql, new ConsumoSetExtractor());
	}
	
	@Override
	public int obtenerConsumosDiarios(int tipoServicio, int codigoContrato) {
		
		String sql = "SELECT COUNT(*) AS cantidad_registros FROM sibusco.restaurante_consumo rc "
				+ "WHERE rc.rts_codigo = " + tipoServicio
				+ "AND rc.rco_codigo = " + codigoContrato
				+ "AND rc.rcn_estado = 1 "
				+ "AND rc.rcn_fecha = CONVERT(DATE, GETDATE())";
		
		int cantidadRegistros = jdbcTemplate.queryForObject(sql, Integer.class);
		
		return cantidadRegistros;
	}

	@Override
	public int registrarConsumo(String userdb, Consumo consumo, int percodigo, int tipoServicio ) {
		
		/**
		String sql = "INSERT INTO sibusco.restaurante_consumo "
				+ "(per_codigo, rve_codigo, rts_codigo, rco_codigo, uaa_codigo, rcn_estado, rcn_fecha, rcn_hora) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
				**/
		
		Date fechaHoraActual = new Date();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		String fechaFormateada = formatoFecha.format(fechaHoraActual);
		
		String sql = "IF NOT EXISTS ( "
				+ "SELECT * "
				+ "FROM sibusco.restaurante_consumo "
				+ "WHERE per_codigo = " + percodigo + " "
				+ "AND rts_codigo = " + tipoServicio + " "
				+ "AND rcn_fecha = CONVERT(DATE, GETDATE()) "
				//+ "AND CONVERT(DATE, '" + fechaFormateada + "') = CONVERT(DATE, GETDATE()) "
				+ ") "
				+ "BEGIN "
				+ "INSERT INTO sibusco.restaurante_consumo "
				+ "(per_codigo, rve_codigo, rts_codigo, rco_codigo, uaa_codigo, rcn_estado, rcn_fecha, rcn_hora) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?) "
				+ "END";

		try {

			int result = jdbcTemplateEjecucion.update(sql,
					new Object[] { 
							consumo.getPersona().getCodigo(), 
							consumo.getVenta().getCodigo(),
							consumo.getTipoServicio().getCodigo(),
							consumo.getContrato().getCodigo(),
							consumo.getDependencia().getCodigo(), 
							consumo.getEstado(),
							consumo.getFecha(), 
							consumo.getHora() });

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int actualizarConsumo(String userdb, Consumo consumo) {
		
		String sql = "UPDATE sibusco.restaurante_consumo " + "SET rcn_estado=? " + "WHERE rcn_codigo=?";

		try {

			int result = jdbcTemplateEjecucion.update(sql, new Object[] { 
					consumo.getEstado(),
					consumo.getCodigo(), });

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
	public List<Long> cargarConsumos(String userdb, List<Consumo> consumos) {
		String sql = 
			    "DECLARE @perCodigo INT; " +
			    "DECLARE @codigoVenta INT; " +
			    "SET @perCodigo = (SELECT e.per_codigo FROM estudiante e WHERE e.est_codigo = :estudianteCodigo); " +
			    "IF @perCodigo IS NULL " +
			    "BEGIN " +
			    "    SET @perCodigo = (SELECT p.per_codigo FROM persona p WHERE p.per_identificacion = :id); " +
			    "END " +
			    "SET @codigoVenta = ( " +
			    "    SELECT rev.rve_codigo " +
			    "    FROM sibusco.restaurante_venta rev " +
			    "    INNER JOIN dbo.persona p ON p.per_codigo = rev.per_codigo " +
			    "    INNER JOIN sibusco.restaurante_tipo_servicio rts ON rts.rts_codigo = rev.rts_codigo " +
			    "    INNER JOIN sibusco.restaurante_contrato rc ON rc.rco_codigo = rev.rco_codigo " +
			    "    INNER JOIN sibusco.restaurante_tipo_contrato rtc ON rtc.rtc_codigo = rc.rtc_codigo " +
			    "    INNER JOIN dbo.uaa u ON u.uaa_codigo = rev.uaa_codigo " +
			    "    WHERE rev.per_codigo = @perCodigo AND rev.rco_codigo = :contrato AND rev.rve_fecha = :fecha " +
			    "    AND rev.rts_codigo = :tipoServicio AND rev.uaa_codigo = :uaa AND rev.rve_estado = 1 " +
			    " 	 ); " +
			    "IF @perCodigo IS NOT NULL " +
			    "BEGIN " +
			    "      IF @codigoVenta IS NOT NULL " +
			    "      BEGIN " +
			    "			UPDATE sibusco.restaurante_venta " +
			    "			SET rve_estado= 0 " +
			    "			WHERE rve_codigo=@codigoVenta " +
			    "           INSERT INTO sibusco.restaurante_consumo " +
			    "           (per_codigo, rve_codigo, rts_codigo, rco_codigo, uaa_codigo, rcn_estado, rcn_fecha, rcn_hora) " +
			    "           VALUES (@perCodigo, @codigoVenta, :tipoServicio, :contrato, :uaa, :estado, :fecha, :hora) " +
			    "      END " +
			    "END; "; 

			    

	    try {
	        SqlParameterSource[] batchParams = consumos.stream()
	                .map(consumo -> new MapSqlParameterSource()
	                        .addValue("estudianteCodigo", consumo.getPersona().getIdentificacion())
	                        .addValue("id", consumo.getPersona().getIdentificacion())
	                        .addValue("tipoServicio", consumo.getTipoServicio().getCodigo())
	                        .addValue("contrato", consumo.getContrato().getCodigo())
	                        .addValue("uaa", consumo.getDependencia().getCodigo())
	                        .addValue("estado", consumo.getEstado())
	                        .addValue("fecha", consumo.getFecha())
	                        .addValue("hora", consumo.getHora())
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
	                registrosErrados.add(Long.parseLong(consumos.get(Math.abs(i)).getPersona().getIdentificacion()));
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
