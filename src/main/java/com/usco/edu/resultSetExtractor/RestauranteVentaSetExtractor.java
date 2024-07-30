package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.RestauranteVenta;
import com.usco.edu.rowMapper.RestauranteVentaRowMapper;

public class RestauranteVentaSetExtractor implements ResultSetExtractor<List<RestauranteVenta>> {
	
	@Override
	public List<RestauranteVenta> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<RestauranteVenta> list = new ArrayList<RestauranteVenta>();
		while (rs.next()) {
			list.add(new RestauranteVentaRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		return list;
	}
}
