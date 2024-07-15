package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.WebParametro;
import com.usco.edu.rowMapper.WebParametroRowMapper;

public class WebParametroSetExtractor implements ResultSetExtractor<List<WebParametro>>{
	
	@Override
	public List<WebParametro> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<WebParametro> list = new ArrayList<WebParametro>();
		while (rs.next()) {
			list.add(new WebParametroRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		
		return list;
	}

}
