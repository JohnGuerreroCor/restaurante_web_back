package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.ReporteVenta;

public class ReporteVentaRowMapper implements RowMapper<ReporteVenta> {
	
	@Override
	public ReporteVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReporteVenta reporteVenta = new ReporteVenta();
		reporteVenta.setCodigo(rs.getInt("rve_codigo"));
		reporteVenta.setPersonaCodigo(rs.getInt("per_codigo"));
		reporteVenta.setPersonaIdentificacion(rs.getString("per_identificacion"));
		reporteVenta.setPersonaNombre(rs.getString("per_nombre")+" "+rs.getString("per_apellido"));
		reporteVenta.setGabu(rs.getString("rtg_nombre"));
		reporteVenta.setTipoServicioCodigo(rs.getInt("rts_codigo"));
		reporteVenta.setTipoServicioNombre(rs.getString("rts_nombre"));
		reporteVenta.setContratoCodigo(rs.getInt("rco_codigo"));
		reporteVenta.setContratoTipoCodigo(rs.getInt("rtc_codigo"));
		reporteVenta.setContratoTipoNombre(rs.getString("rtc_nombre"));
		reporteVenta.setSedeCodigo(rs.getInt("uaa_codigo"));
		reporteVenta.setSedeNombre(rs.getString("uaa_nombre"));
		reporteVenta.setEstado(rs.getInt("rve_estado"));
		reporteVenta.setFecha(rs.getDate("rve_fecha"));
		reporteVenta.setHora(rs.getTime("rve_hora"));
		reporteVenta.setEliminado(rs.getInt("rve_eliminado"));
		return reporteVenta;
	}
}
