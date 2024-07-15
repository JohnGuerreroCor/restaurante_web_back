package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.WebParametro;

public class WebParametroRowMapper implements RowMapper<WebParametro> {
	
	@Override
	public WebParametro mapRow(ResultSet rs, int rowNum) throws SQLException {
		WebParametro webParametro = new WebParametro();
		webParametro.setCodigo(rs.getInt("wep_codigo"));
		webParametro.setWebNonbre(rs.getString("wep_nombre"));
		webParametro.setWebValor(rs.getString("wep_valor"));
		webParametro.setWebDescripcion(rs.getString("wep_descripcion"));
		webParametro.setEstado(rs.getInt("wep_estado"));
		return webParametro;
	}
}
