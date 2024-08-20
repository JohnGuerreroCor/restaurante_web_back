package com.usco.edu.restController;


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
import com.usco.edu.entities.Qr;
import com.usco.edu.service.IConsumoService;
import com.usco.edu.service.IContratoService;
import com.usco.edu.service.IHorarioServicioService;
import com.usco.edu.service.IPersonaService;
import com.usco.edu.service.IVentaService;
import com.usco.edu.service.IWebParametroService;
import com.usco.edu.service.serviceImpl.EncrypDecryptService;

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
	EncrypDecryptService customRSAService;

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
	public int validarConsumo(@PathVariable String username, @PathVariable int uaaCodigo, @RequestBody Qr qr) {
		return consumoService.registrarConsumo(username, uaaCodigo, qr);
	}

}
