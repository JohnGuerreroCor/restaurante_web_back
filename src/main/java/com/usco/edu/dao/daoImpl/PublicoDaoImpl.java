package com.usco.edu.dao.daoImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IPublicoDao;
import com.usco.edu.entities.Administrativo;
import com.usco.edu.entities.Docente;
import com.usco.edu.entities.Estudiante;
import com.usco.edu.entities.Firma;
import com.usco.edu.entities.Graduado;
import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.entities.PoliticaEstamento;
import com.usco.edu.entities.Ticket;
import com.usco.edu.resultSetExtractor.AdministrativoSetExtractor;
import com.usco.edu.resultSetExtractor.DocenteSetExtractor;
import com.usco.edu.resultSetExtractor.EstudianteSetExtractor;
import com.usco.edu.resultSetExtractor.FirmaSetExtractor;
import com.usco.edu.resultSetExtractor.GraduadoSetExtractor;
import com.usco.edu.resultSetExtractor.PersonaSetExtractor;
import com.usco.edu.resultSetExtractor.PoliticaEstamentoSetExtractor;
import com.usco.edu.resultSetExtractor.TicketSetExtractor;

@Repository
public class PublicoDaoImpl implements IPublicoDao {
	
	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<Estudiante> buscarCodigoEstudiante(String codigo) {
		
		String sql = "Select *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from estudiante e "
				+ "inner join persona p on e.per_codigo = p.per_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "inner join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "inner join programa pr on e.pro_codigo = pr.pro_codigo "
				+ "inner join uaa u on pr.uaa_codigo = u.uaa_codigo "
				+ "inner join sede s on pr.sed_codigo = s.sed_codigo "
				+ "where e.est_codigo = '" + codigo + "' ";
		return jdbcTemplate.query(sql, new EstudianteSetExtractor());
		
	}

	@Override
	public List<Estudiante> buscarIdentificacionEstudiante(String id) {
		
		String sql = "Select *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from estudiante e "
				+ "inner join persona p on e.per_codigo = p.per_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "inner join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "inner join programa pr on e.pro_codigo = pr.pro_codigo "
				+ "inner join uaa u on pr.uaa_codigo = u.uaa_codigo "
				+ "inner join sede s on pr.sed_codigo = s.sed_codigo "
				+ "where p.per_identificacion = '" + id + "' order by ins_codigo desc";
		return jdbcTemplate.query(sql, new EstudianteSetExtractor());
		
	}

	@Override
	public List<Graduado> buscarIdentificacionGraduado(String id) {
		
		String sql = "Select top 1 *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from graduado g "
				+ "inner join estudiante e on g.est_codigo = e.est_codigo "
				+ "inner join persona p on e.per_codigo = p.per_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "inner join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "inner join programa pr on e.pro_codigo = pr.pro_codigo "
				+ "inner join uaa u on pr.uaa_codigo = u.uaa_codigo "
				+ "inner join sede s on pr.sed_codigo = s.sed_codigo "
				+ "where p.per_identificacion  = '" + id + "' order by g.gra_codigo desc";
		return jdbcTemplate.query(sql, new GraduadoSetExtractor());
		
	}

	@Override
	public List<Administrativo> buscarIdentificacionAdministrativo(String id) {
		
		String sql = "Exec vinculacion_actual '" + id + "', 0";
		return jdbcTemplate.query(sql, new AdministrativoSetExtractor());
		
	}

	@Override
	public List<Docente> buscarIdentificacionDocente(String id) {
		
		String sql = "Select top 1 *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from docente_vinculacion dv "
				+ "inner join persona p on dv.per_codigo = p.per_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "inner join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "inner join uaa u on dv.uaa_codigo = u.uaa_codigo "
				+ "inner join sede s on u.sed_codigo = s.sed_codigo "
				+ "where dv.per_identificacion  = '" + id + "' order by dv.uap_codigo asc";
		return jdbcTemplate.query(sql, new DocenteSetExtractor());
		
	}

	@Override
	public List<PersonaCarnet> buscarCodigoPersona(int codigo) {
		
		String sql = "Select *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from persona p "
				+ "inner join tipo_id t on p.tii_codigo = t.tii_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo " 
				+ "where per_estado = 1 and per_codigo = " + codigo + " ";
		return jdbcTemplate.query(sql, new PersonaSetExtractor());
		
	}

	@Override
	public List<PersonaCarnet> buscarIdentificacionPersona(String id) {
		
		String sql = "Select *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from persona p "
				+ "inner join tipo_id t on p.tii_codigo = t.tii_codigo "
				+ "inner join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo " 
				+ "where per_estado = 1 and per_identificacion = '" + id + "' ";
		return jdbcTemplate.query(sql, new PersonaSetExtractor());
		
	}

	@Override
	public List<Ticket> obtenerTicketIdentificacion(String identificacion) {
		
		String sql = "select top 1 *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from carnetizacion.ticket_visitante tv "
				+ "left join tercero t on tv.ter_codigo = t.ter_codigo "
				+ "left join persona p on tv.per_codigo = p.per_codigo "
				+ "left join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "left join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "inner join sede s on tv.sed_codigo = s.sed_codigo "
				+ "inner join sub_sede ss on tv.sus_codigo = ss.sus_codigo "
				+ "left join bloque b on tv.blo_codigo = b.blo_codigo "
				+ "left join uaa u on tv.uaa_codigo = u.uaa_codigo "
				+ "where p.per_identificacion = " + identificacion + " or t.ter_identificacion = " + identificacion + " order by tv.tiv_codigo desc";
		return jdbcTemplate.query(sql, new TicketSetExtractor());
		
	}

	@Override
	public String obtenerTokenFoto(String atributos) {
		String sql = "SELECT dbo.getTokenDocumento(?)";
		return jdbcTemplate.queryForObject(sql, new Object[] { atributos }, String.class);
	}

	@Override
	public String obtenerTokenFotoVisualizar(String atributos) {
		String sql = "SELECT dbo.getTokenDocumento(?) as token";
		return jdbcTemplate.queryForObject(sql, new Object[] { atributos }, String.class);
	}

	@Override
	public List<Firma> buscarFirmaActiva() {
		
		String sql = "select *, floor((cast(convert(varchar(8),getdate(),112) as int) - cast(convert(varchar(8), p.per_fecha_nacimiento ,112) as int) ) / 10000) as edad from carnetizacion.firma_digital fd "
				+ "inner join persona p on fd.per_codigo = p.per_codigo "
				+ "left join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "left join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "where fd.fid_estado = 1 and fid_fecha_fin is null ";
		
		return jdbcTemplate.query(sql, new FirmaSetExtractor());
		
	}

	@Override
	public List<PoliticaEstamento> obtenerPoliticaPorCodigoEstamento(int codigo) {
		
		String sql = "select * from carnetizacion.politica_estamento pe "
				+ "inner join dbo.usuario_tipo u on pe.tus_codigo = u.tus_codigo "
				+ "where pe.tus_codigo = " + codigo + "";
		
		return jdbcTemplate.query(sql, new PoliticaEstamentoSetExtractor());
		
	}
	
	@Override
	public String getKeyDocumento(String atributos) {
		String sql = "SELECT dbo.getTokenDocumento(?)";
		return jdbcTemplate.queryForObject(sql, new Object[] { atributos }, String.class);
	}
	@Override
	public String getKeyDocumentomirar(String atributos) {
		String sql = "SELECT dbo.getTokenDocumento(?) as token";
		return jdbcTemplate.queryForObject(sql, new Object[] { atributos }, String.class);
	}

}
