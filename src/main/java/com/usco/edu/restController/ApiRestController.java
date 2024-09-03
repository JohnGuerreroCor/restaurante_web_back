package com.usco.edu.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.service.serviceImpl.EncrypDecryptService;

import java.math.BigInteger;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiRestController {

	@Autowired
	EncrypDecryptService customAESService;

	private static KeyPair keyPair; // Almacena el par de claves

	@GetMapping(path = "hora")
	public String obtenerFechaHoraActual() {
		// Obtener la fecha y hora actual del servidor
		Date fechaHoraActual = new Date();

		// Formatear la fecha y hora como una cadena
		SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);

		// Retornar la fecha y hora formateada como respuesta
		return fechaHoraFormateada;
	}

	@GetMapping(path = "ejemplo")
	public boolean ejemplo() {

		return true;
	}

	@PostMapping("/encrypt")
	public String encryptMessage(@RequestBody String plainString) {

		Date fechaHoraActual = new Date();

		// FORMATEAR LA FECHA Y HORA COMO UNA CADENA
		SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);

		plainString = plainString + "," + fechaHoraFormateada;

		// Encriptar el mensaje
		BigInteger message = new BigInteger(plainString.getBytes());
		BigInteger encryptedMessage = customAESService.encrypt(message);

		// Desencriptar el mensaje encriptado
		BigInteger decryptedMessage = customAESService.decrypt(encryptedMessage);
		String decryptedString = new String(decryptedMessage.toByteArray());
		return encryptedMessage.toString(); // Convertir el mensaje encriptado a una cadena y retornarlo

	}

	@PostMapping("/decrypt")
	public String decryptMessage(@RequestBody String encryptString) {
		return customAESService.decrypt(new BigInteger(encryptString)).toString(); // Decrypt the message
	}

}
