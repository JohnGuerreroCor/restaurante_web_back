package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.CarnetDigital;
import com.usco.edu.rowMapper.CarnetDigitalRowMapper;


public class CarnetDigitalSetExtractor implements ResultSetExtractor<List<CarnetDigital>>{
	
	@Override
	public List<CarnetDigital> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<CarnetDigital> list = new ArrayList<CarnetDigital>();
		while (rs.next()) {
			list.add(new CarnetDigitalRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		
		return list;
	}

}
