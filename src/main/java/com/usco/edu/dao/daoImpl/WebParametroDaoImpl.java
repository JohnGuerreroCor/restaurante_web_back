package com.usco.edu.dao.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IWebParametroDao;
import com.usco.edu.entities.WebParametro;
import com.usco.edu.resultSetExtractor.WebParametroSetExtractor;

@Repository
public class WebParametroDaoImpl implements IWebParametroDao {

	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("JDBCTemplateEjecucion")
	public JdbcTemplate jdbcTemplateEjecucion;

	@Override
	public List<WebParametro> obtenerWebParametro() {

		// actualizar el web parametro una vez se cree en la real
		String sql = "SELECT * FROM web_parametro wp where wp.wep_codigo = 486 " + "OR wp.wep_codigo = 512";
		return jdbcTemplate.query(sql, new WebParametroSetExtractor());

	}

	@Override
	public int actualizarSemilla(byte[] seed) {

		// convertir Byte[] byteSeed to String HexaSeed
		String seedHex = DatatypeConverter.printHexBinary(seed);

		String sql = "UPDATE dbo.web_parametro " + "SET wep_valor=? " + "WHERE wep_codigo=519;";

		try {

			int result = jdbcTemplateEjecucion.update(sql, new Object[] { seedHex });

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public byte[] obtenerSemilla() {

		String sql = "SELECT * FROM dbo.web_parametro wp where wp.wep_codigo = 494;";
		List<WebParametro> data = jdbcTemplate.query(sql, new WebParametroSetExtractor());
		String seedHex = data.get(0).getWebValor();
		byte[] seed = DatatypeConverter.parseHexBinary(seedHex);

		return seed;

	}

	private void cerrarConexion(Connection con) {
		if (con == null)
			return;

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
