package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.RestauranteRaciones;
import com.usco.edu.rowMapper.RestauranteRacionesRowMapper;

public class RestauranteRacionesSetExtractor implements ResultSetExtractor<List<RestauranteRaciones>> {
	
	@Override
	public List<RestauranteRaciones> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<RestauranteRaciones> list = new ArrayList<RestauranteRaciones>();
		while (rs.next()) {
			
			list.add(new RestauranteRacionesRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		
		return list;
		
	}
	
}
