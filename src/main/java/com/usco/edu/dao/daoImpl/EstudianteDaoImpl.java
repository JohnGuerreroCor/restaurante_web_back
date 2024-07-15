package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IEstudianteDao;
import com.usco.edu.entities.Estudiante;
import com.usco.edu.resultSetExtractor.EstudianteSetExtractor;

@Repository
public class EstudianteDaoImpl implements IEstudianteDao{
	
	@Autowired
	@Qualifier("JDBCTemplatePlanesConsulta")
	public JdbcTemplate jdbcTemplate;
	

	@Override
	public List<Estudiante> findByCodigo(String codigo) {
		
		String sql = "DECLARE @est_codigo VARCHAR(50) = '" + codigo + "'; "
				+ "DECLARE @matricula_actual INT;\n"
				+ "\n"
				+ "SET\n"
				+ "@matricula_actual = (\n"
				+ "SELECT\n"
				+ "	wep_valor\n"
				+ "FROM\n"
				+ "	web_parametro\n"
				+ "WHERE\n"
				+ "	wep_nombre = 'MATRICULA_PREGRADO_CALENDARIO_ACTUAL'\n"
				+ ");\n"
				+ "\n"
				+ "IF @matricula_actual > 1\n"
				+ "BEGIN\n"
				+ "		SELECT *, FLOOR((CAST(CONVERT(VARCHAR(8), GETDATE(), 112) AS INT) - CAST(CONVERT(VARCHAR(8), per.per_fecha_nacimiento, 112) AS INT)) / 10000) AS edad FROM estudiante e \n"
				+ "				INNER JOIN persona per ON e.per_codigo = per.per_codigo\n"
				+ "				INNER JOIN programa pro ON e.pro_codigo = pro.pro_codigo\n"
				+ "				INNER JOIN uaa u ON pro.uaa_codigo = u.uaa_codigo\n"
				+ "				INNER JOIN sede s ON pro.sed_codigo = s.sed_codigo\n"
				+ "				INNER JOIN grupo_sanguineo gs ON per.grs_codigo = gs.grs_codigo\n"
				+ "				INNER JOIN tipo_id ti ON per.tii_codigo = ti.tii_codigo\n"
				+ "				INNER JOIN dbo.nivel_academico na ON na.nia_codigo = pro.nia_codigo\n"
				+ "				INNER JOIN dbo.formalidad f ON f.for_codigo = na.for_codigo\n"
				+ "				INNER JOIN dbo.matricula m ON m.est_codigo = e.est_codigo \n"
				+ "				INNER JOIN dbo.calendario c ON c.cal_codigo = m.cal_codigo \n"
				+ "				INNER JOIN dbo.periodo p ON p.per_codigo = c.per_codigo\n"
				+ "				WHERE m.est_codigo = @est_codigo\n"
				+ "				AND c.cal_codigo = @matricula_actual\n"
				+ "				AND f.for_codigo = 1\n"
				+ "				ORDER BY per_fecha_inicio DESC\n"
				+ "END\n"
				+ "ELSE\n"
				+ "BEGIN\n"
				+ "    	SELECT 0;\n"
				+ "END";

		return jdbcTemplate.query(sql, new EstudianteSetExtractor());
		
	}


	@Override
	public List<Estudiante> buscarIdentificacion(String id) {
		
		String sql = "DECLARE @per_identificacion VARCHAR(50) = '" + id + "'; "
				+ "DECLARE @matricula_actual INT;\n"
				+ "\n"
				+ "SET\n"
				+ "@matricula_actual = (\n"
				+ "SELECT\n"
				+ "	wep_valor\n"
				+ "FROM\n"
				+ "	web_parametro\n"
				+ "WHERE\n"
				+ "	wep_nombre = 'MATRICULA_PREGRADO_CALENDARIO_ACTUAL'\n"
				+ ");\n"
				+ "\n"
				+ "IF @matricula_actual > 1\n"
				+ "BEGIN\n"
				+ "		SELECT *, FLOOR((CAST(CONVERT(VARCHAR(8), GETDATE(), 112) AS INT) - CAST(CONVERT(VARCHAR(8), per.per_fecha_nacimiento, 112) AS INT)) / 10000) AS edad FROM estudiante e \n"
				+ "				INNER JOIN persona per ON e.per_codigo = per.per_codigo\n"
				+ "				INNER JOIN programa pro ON e.pro_codigo = pro.pro_codigo\n"
				+ "				INNER JOIN uaa u ON pro.uaa_codigo = u.uaa_codigo\n"
				+ "				INNER JOIN sede s ON pro.sed_codigo = s.sed_codigo\n"
				+ "				INNER JOIN grupo_sanguineo gs ON per.grs_codigo = gs.grs_codigo\n"
				+ "				INNER JOIN tipo_id ti ON per.tii_codigo = ti.tii_codigo\n"
				+ "				INNER JOIN dbo.nivel_academico na ON na.nia_codigo = pro.nia_codigo\n"
				+ "				INNER JOIN dbo.formalidad f ON f.for_codigo = na.for_codigo\n"
				+ "				INNER JOIN dbo.matricula m ON m.est_codigo = e.est_codigo \n"
				+ "				INNER JOIN dbo.calendario c ON c.cal_codigo = m.cal_codigo \n"
				+ "				INNER JOIN dbo.periodo p ON p.per_codigo = c.per_codigo\n"
				+ "				WHERE per.per_identificacion = @per_identificacion \n"
				+ "				AND c.cal_codigo = @matricula_actual\n"
				+ "				AND f.for_codigo = 1\n"
				+ "				ORDER BY per_fecha_inicio DESC\n"
				+ "END\n"
				+ "ELSE\n"
				+ "BEGIN\n"
				+ "    	SELECT 0;\n"
				+ "END";

		return jdbcTemplate.query(sql, new EstudianteSetExtractor());
		
	}

}
