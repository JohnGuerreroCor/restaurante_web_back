package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IReportesDao;
import com.usco.edu.entities.ReporteVenta;
import com.usco.edu.resultSetExtractor.ReporteVentaSetExtractor;

@Repository
public class ReportesDaoImpl implements IReportesDao {


	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<ReporteVenta> obtenerReporteVentas(int sede, String inicio, String fin) {

		String sql = "select * from sibusco.restaurante_venta rv "
				+ "inner join dbo.persona p on rv.per_codigo = p.per_codigo "
				+ "left join sibusco.restaurante_grupo_gabu rgg on rv.per_codigo = rgg.per_codigo "
				+ "left join sibusco.restaurante_tipo_gabu rtg on rgg.rtg_codigo = rtg.rtg_codigo "
				+ "inner join sibusco.restaurante_tipo_servicio rts on rv.rts_codigo = rts.rts_codigo "
				+ "inner join sibusco.restaurante_contrato rc on rv.rco_codigo = rc.rco_codigo "
				+ "inner join sibusco.restaurante_tipo_contrato rtc on rc.rtc_codigo = rtc.rtc_codigo "
				+ "inner join sibusco.restaurante_sede rs on rv.uaa_codigo = rs.uaa_codigo "
				+ "where rv.rve_eliminado != 0 and rv.uaa_codigo =  ? and CONVERT(DATE, rv.rve_fecha) BETWEEN ? AND ? "
				+ "ORDER BY rv.rve_fecha desc, rv.rts_codigo asc; ";
		
		return jdbcTemplate.query(sql, new ReporteVentaSetExtractor(), sede, inicio, fin);
		
	}
	
}