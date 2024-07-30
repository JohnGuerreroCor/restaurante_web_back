package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IEstamentoDao;
import com.usco.edu.entities.CarnetDigital;
import com.usco.edu.entities.Estamento;
import com.usco.edu.resultSetExtractor.EstamentoSetExtractor;
import com.usco.edu.resultSetExtractor.CarnetDigitalSetExtractor;

@Repository
public class EstamentoDaoImpl implements IEstamentoDao {
	

	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<Estamento> estamentos(String userdb) {

		String sql = "select * from usuario_tipo where tus_estado = 1";
		return jdbcTemplate.query(sql, new EstamentoSetExtractor());

	}

	@Override
	public List<Estamento> carnets(int percodigo) {
		
		String sql = "select ut.tus_codigo, ut.tus_nombre from carnetizacion.usuario_carnet_digital ucd "
				+ "inner join usuario_tipo ut on ucd.istipo = ut.tus_codigo "
				+ "where ucd.up = " + percodigo + " group by ut.tus_codigo, ut.tus_nombre ";
		return jdbcTemplate.query(sql, new EstamentoSetExtractor());
		
	}

	@Override
	public List<CarnetDigital> carnetEstamento(int percodigo) {
		
		String sql = "select ucd.usg_usuario, ut.tus_codigo, ut.tus_nombre, ucd.usg_uaa, ucd.us from carnetizacion.usuario_carnet_digital ucd "
				+ "inner join usuario_tipo ut on ucd.istipo = ut.tus_codigo "
				+ "where ucd.up = " + percodigo + " group by ucd.usg_usuario, ut.tus_codigo, ut.tus_nombre, ucd.usg_uaa, ucd.us "
				+ "order by ucd.usg_usuario desc";
		return jdbcTemplate.query(sql, new CarnetDigitalSetExtractor());
		
	}

}
