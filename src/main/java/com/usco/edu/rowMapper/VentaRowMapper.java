package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.usco.edu.entities.Venta;

public class VentaRowMapper implements RowMapper<Venta> {

	@Override
	public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
		Venta venta = new Venta();
		venta.setCodigo(rs.getInt("rve_codigo"));
		venta.setPersona(new PersonaRowMapper().mapRow(rs, rowNum));
		venta.setTipoServicio(new TipoServicioRowMapper().mapRow(rs, rowNum));
		venta.setContrato(new ContratoRowMapper().mapRow(rs, rowNum));
		venta.setDependencia(new DependenciaRowMapper().mapRow(rs, rowNum));
		venta.setEstado(rs.getInt("rve_estado"));
		venta.setHora(rs.getTime("rve_hora"));
		venta.setFecha(rs.getDate("rve_fecha"));
		venta.setEliminado(rs.getInt("rve_eliminado"));
		return venta;
	}

}
