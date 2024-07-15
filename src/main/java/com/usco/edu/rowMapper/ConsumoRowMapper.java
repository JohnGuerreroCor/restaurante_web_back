package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.Consumo;

public class ConsumoRowMapper implements RowMapper<Consumo> {

	@Override
	public Consumo mapRow(ResultSet rs, int rowNum) throws SQLException {
		Consumo consumo = new Consumo();
		consumo.setCodigo(rs.getInt("rcn_codigo"));
		consumo.setPersona(new PersonaRowMapper().mapRow(rs, rowNum));
		consumo.setTipoServicio(new TipoServicioRowMapper().mapRow(rs, rowNum));
		consumo.setContrato(new ContratoRowMapper().mapRow(rs, rowNum));
		consumo.setDependencia(new DependenciaRowMapper().mapRow(rs, rowNum));
		consumo.setVenta(new VentaRowMapper().mapRow(rs, rowNum));
		consumo.setEstado(rs.getInt("rcn_estado"));
		consumo.setFecha(rs.getDate("rcn_fecha"));
		consumo.setHora(rs.getTime("rcn_hora"));
		return consumo;
	}

}
