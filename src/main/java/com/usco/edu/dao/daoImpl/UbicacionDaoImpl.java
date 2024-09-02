package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IUbicacionDao;
import com.usco.edu.entities.SedeCarnet;
import com.usco.edu.entities.SubSede;
import com.usco.edu.resultSetExtractor.SedeCarnetSetExtractor;
import com.usco.edu.resultSetExtractor.SubSedeSetExtractor;

@Repository
public class UbicacionDaoImpl implements IUbicacionDao {
	
	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<SedeCarnet> obtenerSedes(String userdb) {
		
		String sql = "select * from dbo.sede s where s.sed_estado = 1 and s.sippa_sed_codigo is not null";
		return jdbcTemplate.query(sql, new SedeCarnetSetExtractor());
		
	}

	@Override
	public List<SubSede> obtenerSubSedes(String userdb) {
		
		String sql = "select * from dbo.sub_sede ss";
		return jdbcTemplate.query(sql, new SubSedeSetExtractor());
		
	}


	@Override
	public List<SubSede> buscarSubSedePorSede(int codigo, String userdb) {
		
		String sql = "select * from dbo.sub_sede ss where ss.sed_codigo = " + codigo + " ";
		return jdbcTemplate.query(sql, new SubSedeSetExtractor());
	
	}

}
