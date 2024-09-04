package com.usco.edu.resultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.usco.edu.entities.ReporteVenta;
import com.usco.edu.rowMapper.ReporteVentaRowMapper;

public class ReporteVentaSetExtractor implements ResultSetExtractor<List<ReporteVenta>> {

	@Override
	public List<ReporteVenta> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<ReporteVenta> list = new ArrayList<ReporteVenta>();
		while (rs.next()) {
			list.add(new ReporteVentaRowMapper().mapRow(rs, (rs.getRow() - 1)));
		}
		return list;
	}
}
