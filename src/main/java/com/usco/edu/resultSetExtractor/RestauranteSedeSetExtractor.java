package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.RestauranteSede;
import com.usco.edu.rowMapper.RestauranteSedeRowMapper;

public class RestauranteSedeSetExtractor implements ResultSetExtractor<List<RestauranteSede>> {
	
	@Override
	public List<RestauranteSede> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<RestauranteSede> list = new ArrayList<RestauranteSede>();
		while (rs.next()) {
			
			list.add(new RestauranteSedeRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		
		return list;
		
	}
}
