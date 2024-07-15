package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.CarnetDigital;

public class CarnetDigitalRowMapper implements RowMapper<CarnetDigital>{
	
	@Override
	public CarnetDigital mapRow(ResultSet rs, int rowNum) throws SQLException {
		CarnetDigital bloque = new CarnetDigital();
		bloque.setCodigo(rs.getInt("tus_codigo"));
		bloque.setEstamento(rs.getString("tus_nombre"));
		bloque.setUaa(rs.getInt("usg_uaa"));
		bloque.setUsuario(rs.getString("us"));
		return bloque;
	}

}
