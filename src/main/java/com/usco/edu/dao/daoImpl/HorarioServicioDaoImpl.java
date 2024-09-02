package com.usco.edu.dao.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IHorarioServicioDao;
import com.usco.edu.entities.HorarioServicio;
import com.usco.edu.resultSetExtractor.HorarioServicioSetExtractor;
import com.usco.edu.util.AuditoriaJdbcTemplate;

@Repository
public class HorarioServicioDaoImpl implements IHorarioServicioDao {

	@Autowired
	private AuditoriaJdbcTemplate jdbcComponent;

	@Autowired
	@Qualifier("JDBCTemplateConsulta")
	public JdbcTemplate jdbcTemplate;

	@Override
	public List<HorarioServicio> obtenerHorariosServicio(String userdb) {
		String sql = "SELECT * FROM sibusco.restaurante_horario_servicio rhs "
				+ "LEFT JOIN sibusco.restaurante_tipo_servicio rts ON rts.rts_codigo = rhs.rhs_codigo "
				+ "LEFT JOIN dbo.uaa ua ON ua.uaa_codigo = rhs.rhs_uaa_codigo "
				+ "WHERE rhs.rhs_estado = 1";

		return jdbcTemplate.query(sql, new HorarioServicioSetExtractor());
	}

	@Override
	public List<HorarioServicio> obtenerHorarioServicio(String userdb, int codigo) {
		String sql = "SELECT * FROM sibusco.restaurante_horario_servicio rhs "
				+ "	INNER JOIN sibusco.restaurante_tipo_servicio rts ON rts.rts_codigo = rhs.rts_codigo "
				+ "	INNER JOIN dbo.uaa u ON u.uaa_codigo = rhs.rhs_uaa_codigo "
				+ "	WHERE rhs.rhs_uaa_codigo = " + codigo + " "
				+ "	AND rhs.rhs_estado = 1;";
		return jdbcTemplate.query(sql, new HorarioServicioSetExtractor());
	}

	@Override
	public int crearHorarioServicio(String userdb, HorarioServicio horarioServicio) {
		DataSource dataSource = jdbcComponent.construirDataSourceDeUsuario(userdb);
		NamedParameterJdbcTemplate jdbc = jdbcComponent.construirTemplatenew(dataSource);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO sibusco.restaurante_horario_servicio "
				+ "(rhs_hora_inicio_venta, rhs_hora_fin_venta, rhs_hora_inicio_atencion, rhs_hora_fin_atencion, rts_codigo, rhs_uaa_codigo, rhs_estado, rhs_observacion_estado, rhs_fecha_estado, rhs_cantidad_comidas) "
				+ "VALUES(:horaIncioVenta, :horaFinVenta, :horaInicioAtencion, :horaFinAtencion, :tipoServicio, :uaa, :estado, :observacionEstado, :fechaEstado, :cantidadComidas);";

		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();

			parameter.addValue("horaIncioVenta", horarioServicio.getHoraIncioVenta());
			parameter.addValue("horaFinVenta", horarioServicio.getHoraFinVenta());
			parameter.addValue("horaInicioAtencion", horarioServicio.getHoraInicioAtencion());
			parameter.addValue("horaFinAtencion", horarioServicio.getHoraFinAtencion());
			parameter.addValue("tipoServicio", horarioServicio.getTipoServicio().getCodigo());
			parameter.addValue("uaa", horarioServicio.getUaa());
			parameter.addValue("estado", horarioServicio.getEstado());
			parameter.addValue("observacionEstado", horarioServicio.getObservacionEstado());
			parameter.addValue("fechaEstado", horarioServicio.getFechaEstado());
			parameter.addValue("cantidadComidas", horarioServicio.getCantidadComidas());

			jdbc.update(sql, parameter, keyHolder);
			return keyHolder.getKey().intValue();

		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		} finally {
			try {
				cerrarConexion(dataSource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public int actualizarHorarioServicio(String userdb, HorarioServicio horarioServicio) {
		DataSource dataSource = jdbcComponent.construirDataSourceDeUsuario(userdb);
		NamedParameterJdbcTemplate jdbc = jdbcComponent.construirTemplatenew(dataSource);

		String sql = "UPDATE sibusco.restaurante_horario_servicio "
				+ "SET rhs_hora_inicio_venta=:horaIncioVenta, rhs_hora_fin_venta=:horaFinVenta, rhs_hora_inicio_atencion=:horaInicioAtencion, rhs_hora_fin_atencion=:horaFinAtencion, rts_codigo=:tipoServicio, rhs_uaa_codigo=:uaa, rhs_estado=:estado, rhs_observacion_estado=:observacionEstado, rhs_fecha_estado=:fechaEstado, rhs_cantidad_comidas=:cantidadComidas "
				+ "WHERE rhs_codigo=:codigo";

		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();

			parameter.addValue("codigo", horarioServicio.getCodigo());
			parameter.addValue("horaIncioVenta", horarioServicio.getHoraIncioVenta());
			parameter.addValue("horaFinVenta", horarioServicio.getHoraFinVenta());
			parameter.addValue("horaInicioAtencion", horarioServicio.getHoraInicioAtencion());
			parameter.addValue("horaFinAtencion", horarioServicio.getHoraFinAtencion());
			parameter.addValue("tipoServicio", horarioServicio.getTipoServicio().getCodigo());
			parameter.addValue("uaa", horarioServicio.getUaa().getCodigo());
			parameter.addValue("estado", horarioServicio.getEstado());
			parameter.addValue("observacionEstado", horarioServicio.getObservacionEstado());
			parameter.addValue("fechaEstado", horarioServicio.getFechaEstado());
			parameter.addValue("cantidadComidas", horarioServicio.getCantidadComidas());

			return jdbc.update(sql, parameter);
		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		} finally {
			try {
				cerrarConexion(dataSource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public HorarioServicio obtenerTipoServicioActual(int uaa) throws NullPointerException {
		String sql = "SELECT * "
				+ "FROM sibusco.restaurante_horario_servicio rhs "
				+ "INNER JOIN sibusco.restaurante_tipo_servicio rts ON rts.rts_codigo = rhs.rts_codigo "
				+ "INNER JOIN dbo.uaa u ON u.uaa_codigo = rhs.rhs_uaa_codigo "
				+ "WHERE CONVERT(TIME, GETDATE()) >= rhs.rhs_hora_inicio_atencion "
				+ "  AND CONVERT(TIME, GETDATE()) <= rhs.rhs_hora_fin_atencion "
				+ "  AND rhs.rhs_uaa_codigo = " + uaa + " ;";
		return jdbcTemplate.query(sql, new HorarioServicioSetExtractor()).get(0);
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
