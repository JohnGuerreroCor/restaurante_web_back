package com.usco.edu.service.serviceImpl;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.edu.dao.IPublicoDao;
import com.usco.edu.dto.FotoAntigua;
import com.usco.edu.dto.RespuestaVerArchivo;
import com.usco.edu.dto.RespuestaVerFoto;
import com.usco.edu.entities.Administrativo;
import com.usco.edu.entities.Docente;
import com.usco.edu.entities.Estudiante;
import com.usco.edu.entities.Firma;
import com.usco.edu.entities.Graduado;
import com.usco.edu.entities.PersonaCarnet;
import com.usco.edu.entities.PoliticaEstamento;
import com.usco.edu.entities.Ticket;
import com.usco.edu.feing.EnviarArchivoClient;
import com.usco.edu.feing.EnviarFotoClient;
import com.usco.edu.service.IPublicoService;

@Service
public class PublicoServiceImpl implements IPublicoService {
	
	@Autowired
	private IPublicoDao publicoDao;
	
	@Autowired
	private EnviarFotoClient enviarFotoClient;
	
	@Autowired
	private EnviarArchivoClient enviarArchivo;

	@Override
	public List<Estudiante> buscarCodigoEstudiante(String codigo) {
		
		return publicoDao.buscarCodigoEstudiante(codigo);
				
	}

	@Override
	public List<Estudiante> buscarIdentificacionEstudiante(String id) {
		
		return publicoDao.buscarIdentificacionEstudiante(id);
		
	}

	@Override
	public List<Graduado> buscarIdentificacionGraduado(String id) {
		
		return publicoDao.buscarIdentificacionGraduado(id);
		
	}

	@Override
	public List<Administrativo> buscarIdentificacionAdministrativo(String id) {
		
		return publicoDao.buscarIdentificacionAdministrativo(id);
		
	}

	@Override
	public List<Docente> buscarIdentificacionDocente(String id) {
		
		return publicoDao.buscarIdentificacionDocente(id);
		
	}

	@Override
	public List<PersonaCarnet> buscarCodigoPersona(int codigo) {
		
		return publicoDao.buscarCodigoPersona(codigo);
		
	}

	@Override
	public List<PersonaCarnet> buscarIdentificacionPersona(String id) {
		
		return publicoDao.buscarIdentificacionPersona(id);
	}

	@Override
	public List<Ticket> obtenerTicketIdentificacion(String identificacion) {
		
		return publicoDao.obtenerTicketIdentificacion(identificacion);
		
	}

	@Override
	public ByteArrayInputStream mirarFoto(String perCodigo, HttpServletResponse response) {
		
		byte[] array = {1, 2, 3, 4};

		String Key = publicoDao.obtenerTokenFoto(perCodigo + "");

		RespuestaVerFoto respuesta = new RespuestaVerFoto();

		try {
			respuesta = enviarFotoClient.mostrarFoto(perCodigo, Key);

			byte[] fotoBytes = Base64.getDecoder().decode(respuesta.getBase64().split(",")[1]);
			return new ByteArrayInputStream(fotoBytes);

		} catch (Exception e) {
			System.out.println(e);
		}

		return new ByteArrayInputStream(array);
	}
	
	@Override
	public ByteArrayInputStream mirarArchivo(long archivoCodigo, HttpServletResponse response) {
		String Key = publicoDao.getKeyDocumento(archivoCodigo+"");

		RespuestaVerArchivo respuesta = new RespuestaVerArchivo();

		try {
			respuesta = enviarArchivo.mostrarArchivo(archivoCodigo, Key);

			byte[] archivoBytes = Base64.getDecoder().decode(respuesta.getBase64().split(",")[1]);
			return new ByteArrayInputStream(archivoBytes);

		} catch (Exception e) {
			System.out.println(e);
		}

		return null;
	}
	
	@Override
	public FotoAntigua mirarFotoAntigua(String perCodigo) {
		String aux = "";
		String Key = publicoDao.obtenerTokenFoto(perCodigo);
		aux = enviarFotoClient.mostrarFotoAntigua(perCodigo, Key).toString();
		FotoAntigua foto = new FotoAntigua();
		foto.setUrl(aux);
		return foto;
	}

	@Override
	public List<Firma> buscarFirmaActiva() {
		return publicoDao.buscarFirmaActiva();
	}

	@Override
	public List<PoliticaEstamento> obtenerPoliticaPorCodigoEstamento(int codigo) {
		return publicoDao.obtenerPoliticaPorCodigoEstamento(codigo);
	}

}
