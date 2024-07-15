package com.usco.edu.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usco.edu.service.serviceImpl.CustomRSAServiceImpl;
import com.usco.edu.service.serviceImpl.EncryptDecryptServiceImpl;

import java.math.BigInteger;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiRestController {

	@Autowired
	EncryptDecryptServiceImpl encryptDecryptServiceImpl;

	@Autowired
	CustomRSAServiceImpl customRSAService;

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

	@GetMapping("/createKeys")
	public void createPrivatePublickey() {
		encryptDecryptServiceImpl.createKeys();
	}

	@PostMapping("/encrypt")
	public String encryptMessage(@RequestBody String plainString) {

		System.out.println("Agregando time signature");

		Date fechaHoraActual = new Date();

		// FORMATEAR LA FECHA Y HORA COMO UNA CADENA
		SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);

		plainString = plainString + "," + fechaHoraFormateada;

		// Encriptar el mensaje
		BigInteger message = new BigInteger(plainString.getBytes());
		System.out.println("-----------------------------------------------------------------");
		BigInteger encryptedMessage = customRSAService.encrypt(message);
		System.out.println("Mensaje encriptado BIGINTEGER: " + encryptedMessage);
		System.out.println("Mensaje encriptado STRING: " + encryptedMessage.toString());
		System.out.println("-----------------------------------------------------------------");

		// Desencriptar el mensaje encriptado
		BigInteger decryptedMessage = customRSAService.decrypt(encryptedMessage);
		System.out.println("Mensaje desencriptado: " + decryptedMessage);
		String decryptedString = new String(decryptedMessage.toByteArray());
		System.out.println("Mensaje desencriptado como cadena: " + decryptedString);
		return encryptedMessage.toString(); // Convertir el mensaje encriptado a una cadena y retornarlo

	}

	@PostMapping("/decrypt")
	public String decryptMessage(@RequestBody String encryptString) {
		return customRSAService.decrypt(new BigInteger(encryptString)).toString(); // Decrypt the message
	}

}
