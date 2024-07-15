package com.usco.edu.restController;

import java.math.BigInteger;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.Consumo;
import com.usco.edu.entities.Contrato;
import com.usco.edu.entities.Dependencia;
import com.usco.edu.entities.HorarioServicio;
import com.usco.edu.entities.Persona;
import com.usco.edu.entities.Qr;
import com.usco.edu.entities.TipoServicio;
import com.usco.edu.entities.Venta;
import com.usco.edu.service.IConsumoService;
import com.usco.edu.service.IContratoService;
import com.usco.edu.service.IHorarioServicioService;
import com.usco.edu.service.IPersonaService;
import com.usco.edu.service.IVentaService;
import com.usco.edu.service.IWebParametroService;
import com.usco.edu.service.serviceImpl.CustomRSAServiceImpl;
import com.usco.edu.service.serviceImpl.EncryptDecryptServiceImpl;

@RestController
@RequestMapping(path = "consumo")
public class ConsumoRestController {

	@Autowired
	private IConsumoService consumoService;

	@Autowired
	private IContratoService contratoService;

	@Autowired
	private IVentaService ventaService;

	@Autowired
	private IHorarioServicioService horarioServicioService;

	@Autowired
	private IWebParametroService webParametroService;

	@Autowired
	private IPersonaService personaService;

	@Autowired
	EncryptDecryptServiceImpl encryptDecryptServiceImpl;
	
	@Autowired
	CustomRSAServiceImpl customRSAService;

	@GetMapping(path = "ejemplo")
	public boolean ejemplo() {

		return true;
	}

	@GetMapping(path = "obtener-consumo/{username}/{codigoPersona}/{codigoContrato}")
	public List<Consumo> obtenerConsumoByPerCodigo(@PathVariable String username, @PathVariable int codigoPersona,
			@PathVariable int codigoContrato) {
		return consumoService.obtenerConsumoByPerCodigo(username, codigoPersona, codigoContrato);
	}
	
	@GetMapping(path = "obtener-consumos-diarios/{codigoTipoServicio}/{CodigoContrato}")
	public int obtenerConsumosDiarios(@PathVariable int codigoTipoServicio, @PathVariable int CodigoContrato) {
		return consumoService.obtenerConsumosDiarios(codigoTipoServicio, CodigoContrato);
	}

	@PutMapping(path = "actualizar-consumo/{username}")
	public int actualizarConsumo(@PathVariable String username, @RequestBody Consumo consumo) {
		return consumoService.actualizarConsumo(username, consumo);
	}
	
	@PostMapping(path = "cargue-informacion/{username}")
	public List<Long> cargueInformacion(@PathVariable String username, @RequestBody List<Consumo> consumos) {
		return consumoService.cargarConsumos(username, consumos);
	}

	@PutMapping(path = "validar-consumo/{username}/{uaaCodigo}")
	public int validarConsumo(@PathVariable String username, @PathVariable int uaaCodigo,
			@RequestBody Qr qr) {
		
		String stringEncriptado = qr.getMensajeEncriptado().toString().trim();	
		
		System.out.println(stringEncriptado);
		
		String[] arregloDeDatos = desencriptarCustomQR(stringEncriptado);	
		
		if (arregloDeDatos == null) {
			return -2;
		}

		int estudiante = Integer.parseInt(arregloDeDatos[0]);

		// Imprimir los datos resultantes
		for (String dato : arregloDeDatos) {
			System.out.println(dato);
		}

		Boolean isQRValido = validarQR(arregloDeDatos[1]);

		if (isQRValido) {

			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

			String[] fechaHoraFormateada = this.formatearFechaHora(formatoFecha, formatoHora); // nice

			String fechaFormateada = fechaHoraFormateada[0];
			String horaFormateada = fechaHoraFormateada[1];

			// username,uaaCodigo
			List<Contrato> contratoVigente = this.contratoService.obtenerContratosByVigencia(username, uaaCodigo,
					fechaFormateada);

			if (contratoVigente.isEmpty()) {
				return -3;
			}

			System.out.println("contrato vigente");
			System.out.println(contratoVigente.get(0).getCodigo());

			// obtengo los horario servicio segun uaa
			List<HorarioServicio> horarioServicio = this.horarioServicioService.obtenerHorarioServicio(username,
					uaaCodigo);

			int tipoServicioActual = obtenerTipoServicioActual(horarioServicio, horaFormateada);

			Venta ventaAntigua = this.obtenerVentaAntigua(username, estudiante, contratoVigente, tipoServicioActual);

			try {

				// validar que venta antigua no venga vacia
				if (ventaAntigua == null) {
					return 0;
				}

				int consumoCreado = this.crearConsumo(ventaAntigua, contratoVigente,
						ventaAntigua.getDependencia().getCodigo(), fechaFormateada, horaFormateada, estudiante,
						tipoServicioActual, username, formatoFecha, formatoHora);

				String idEstudiante = this.personaService.buscarPorPerCodigo(String.valueOf(estudiante)).get(0)
						.getIdentificacion();
				
				System.out.println("consumo creado");
				System.out.println(consumoCreado);

				if (consumoCreado == 1) {
					return Integer.parseInt(idEstudiante);
				} else {
					return 0;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("Error al crear el consumo!");
				return -1;
			}

		} else {
			System.out.println("EL QR leido está vencido o es de caracter invalido!");
			return -2;
		}

	}

	private int crearConsumo(Venta ventaAntigua, List<Contrato> contratoVigente, int uaa, String fechaFormateada,
			String horaFormateada, int estudiante, int tipoServicioActual, String username,
			SimpleDateFormat formatoFecha, SimpleDateFormat formatoHora) throws ParseException {
		// ahora creo un consumo y asocio la venta al consumo creado, acto seguido
		// desactivo dicha venta

		if (ventaAntigua != null) {

			Consumo consumo = new Consumo();

			Contrato contrato = new Contrato();
			contrato.setCodigo(contratoVigente.get(0).getCodigo());
			consumo.setContrato(contrato);
			Dependencia dependencia = new Dependencia();
			dependencia.setCodigo(uaa);
			consumo.setDependencia(dependencia);
			consumo.setEstado(1);

			Date fechaEnviar = formatoFecha.parse(fechaFormateada);
			java.sql.Date sqlDate = new java.sql.Date(fechaEnviar.getTime());
			consumo.setFecha(sqlDate);
			Date date = formatoHora.parse(horaFormateada);
			long milisegundos = date.getTime();
			Time horaEnviar = new Time(milisegundos);
			consumo.setHora(horaEnviar);

			Persona persona = new Persona();
			persona.setCodigo(Long.valueOf(estudiante));
			consumo.setPersona(persona);
			TipoServicio tipoServicio = new TipoServicio();
			tipoServicio.setCodigo(tipoServicioActual);
			consumo.setTipoServicio(tipoServicio);
			Venta venta = new Venta();
			venta.setCodigo(ventaAntigua.getCodigo());
			consumo.setVenta(venta);

			int resConsumo = this.consumoService.registrarConsumo(username, consumo, estudiante, tipoServicioActual);

			System.out.println(resConsumo);

			if (resConsumo != 0) {
				System.out.println("consumo creado!");
			}

			this.desactivarTiqueteVenta(resConsumo, ventaAntigua, username);
			return resConsumo;

		} else {
			System.out.println("No se ha encontrado un tiquete con las caracteristicas validas para ser redimido!");
			return 0;
		}
	}

	private int desactivarTiqueteVenta(int resConsumo, Venta ventaAntigua, String username) {
		if (resConsumo > 0) {
			Venta ventaEnviar = new Venta();
			ventaEnviar.setCodigo(ventaAntigua.getCodigo());
			ventaEnviar.setEstado(0);
			this.ventaService.actualizarVenta(username, ventaEnviar);
			return 1;
		} else {
			System.out.println("error al desactivar tiquete venta!");
			return 0;
		}
	}

	private Venta obtenerVentaAntigua(String username, int estudiante, List<Contrato> contratoVigente,
			int tipoServicioActual) {
		// una vez identificado el tipo de servicio, de las ventas obtenidas, desactivo
		// la mas antigua que corresponda a ese tipo de servicio (si tiene para el tipo
		// de servicio correspondiente)
		List<Venta> ventas = this.ventaService.obtenerVentasByPerCodigo(username, estudiante,
				contratoVigente.get(0).getCodigo());

		Date fechaMasAntigua = new Date();
		Venta ventaAntigua = null;

		try {

			for (Venta dato : ventas) {
				if (dato.getTipoServicio().getCodigo() == tipoServicioActual) {
					fechaMasAntigua = dato.getFecha();
					ventaAntigua = dato;

					System.out.println("fecha mas antigua");
					System.out.println(fechaMasAntigua);
					System.out.println("venta antigua");
					System.out.println(ventaAntigua);

					return ventaAntigua;
				}
			}

		} catch (NullPointerException e) {
			System.out.println("Error al obtener venta antigua!");
			return null;
		}

		return null;
	}

	private static int obtenerTipoServicioActual(List<HorarioServicio> horarioServicio, String horaFormateada) {
		int tipoServicioActual = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		for (HorarioServicio dato : horarioServicio) {
			try {
				// Convertir Time a String usando SimpleDateFormat
				String horaInicioStr = sdf.format(dato.getHoraInicioAtencion());
				String horaFinStr = sdf.format(dato.getHoraFinAtencion());

				// Convertir las cadenas de hora a objetos LocalTime
				LocalTime horaInicio = LocalTime.parse(horaInicioStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
				LocalTime horaFin = LocalTime.parse(horaFinStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
				LocalTime horaActual = LocalTime.parse(horaFormateada, DateTimeFormatter.ofPattern("HH:mm:ss"));

				// Realizar la comparación de horas
				if (horaActual.isAfter(horaInicio) && horaActual.isBefore(horaFin)) {
					tipoServicioActual = dato.getTipoServicio().getCodigo();
					System.out.println("tipo servicio actual: ");
					System.out.println(tipoServicioActual);
					return tipoServicioActual;
				}

				System.out.println(dato);

			} catch (Exception e) {
				System.out.println("Error al obtener el tipo de servicio!");
				return 0;
			}

		}

		return 0;
	}

	private String[] formatearFechaHora(SimpleDateFormat formatoFecha, SimpleDateFormat formatoHora) {

		try {
			// Obtener la fecha y hora actual del servidor
			Date fechaHoraActual = new Date();

			// Formatear la fecha y hora como una cadena
			String fechaFormateada = formatoFecha.format(fechaHoraActual);
			String horaFormateada = formatoHora.format(fechaHoraActual);

			// System.out.println("fecha formateada");
			// System.out.println(fechaFormateada);
			return new String[] { fechaFormateada, horaFormateada };

		} catch (Error e) {
			System.out.println("Error al formatear la fecha y hora!");
			return null;
		}
	}

	private String[] desencriptarQR(String stringEncriptado) {
		try {
			String textoDesencriptado = this.encryptDecryptServiceImpl.decryptMessage(stringEncriptado);
			String[] arregloDeDatos = textoDesencriptado.split(",");
			return arregloDeDatos;
		} catch (Exception e) {
			System.out.println("Error al desencriptar el QR!");
			return null;
		}
	}
	
	private String[] desencriptarCustomQR(String stringEncriptado) {
		try {
			// Desencriptar el mensaje encriptado
			BigInteger decryptedMessage = customRSAService.decrypt(new BigInteger(stringEncriptado));
			System.out.println("Mensaje desencriptado: " + decryptedMessage);
			String decryptedString = new String(decryptedMessage.toByteArray());
			System.out.println("Mensaje desencriptado como cadena: " + decryptedString);
			
            //String textoDesencriptado = customRSAService.decrypt(new BigInteger(stringEncriptado)).toString();
            String[] arregloDeDatos = decryptedString.split(",");
            return arregloDeDatos;
        } catch (Exception e) {
            System.out.println("Error al desencriptar el QR!");
            return null;
        }
	}

	private boolean validarQR(String fechaHoraCliente) {

		try {
			int validezQR = Integer.parseInt(this.webParametroService.obtenerWebParametro().get(1).getWebValor());
			System.out.println("validezQR en segundos: ");
			System.out.println(validezQR);

			// Obtener la fecha y hora actual del servidor
			Date currentFechaHora = new Date();

			// Convertir la fecha del cliente a objeto Date
			// Date fechaCliente = new Date(fechaHoraCliente);

			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fechaCliente;

			try {
				fechaCliente = formato.parse(fechaHoraCliente);
				System.out.println("Fecha convertida: " + fechaCliente);
			} catch (ParseException e) {
				System.out.println("ERROR al transformar la fecha hora de un String a un objeto Date ");
				e.printStackTrace();
				return false;
			}

			// Calcular la fecha y hora límite
			Date fechaLimite = new Date(fechaCliente.getTime() + validezQR * 1000);

			// Comparar si la fecha del cliente es anterior a la fecha límite

			System.out.println("Current fecha hora ");
			System.out.println(currentFechaHora);
			System.out.println("fecha generacion ");
			System.out.println(fechaCliente);
			System.out.println("fecha limite ");
			System.out.println(fechaLimite);

			boolean isQRValido = currentFechaHora.before(fechaLimite);

			// Imprimir el resultado
			System.out.println("¿La fecha actual es inferior a la fecha limite? " + isQRValido);
			return isQRValido;
		} catch (Exception e) {
			System.out.println("Error al validar la expiracion del QR!");
			e.printStackTrace();
			return false;
		}

	}

}
