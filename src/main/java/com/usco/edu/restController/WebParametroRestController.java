package com.usco.edu.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.entities.WebParametro;
import com.usco.edu.service.IWebParametroService;

@RestController
@RequestMapping(path = "webparametro")

public class WebParametroRestController {
	
	@Autowired
	IWebParametroService webParametroServie;
	
	@GetMapping(path = "obtenerWebParametro")
	public List<WebParametro> obtenerWebParametro() {
		
		return webParametroServie.obtenerWebParametro();
		
	}
	
	@GetMapping(path = "obtenerSemilla")
	public byte[] obtenerSemilla() {
		
		return webParametroServie.obtenerSemilla();
		
	}

}
