package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.RestauranteTiquetes;
import com.usco.edu.rowMapper.RestauranteTiquetesRowMapper;

public class RestauranteTiquetesSetExtractor implements ResultSetExtractor<List<RestauranteTiquetes>> {
	
	@Override
	public List<RestauranteTiquetes> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<RestauranteTiquetes> list = new ArrayList<RestauranteTiquetes>();
		while (rs.next()) {
			
			list.add(new RestauranteTiquetesRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		
		return list;
		
	}
}
