package com.usco.edu.service.serviceImpl;

import java.security.KeyPair;
import java.security.Security;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.usco.edu.service.IWebParametroService;

@Service
@EnableScheduling
public class EncryptDecryptServiceImpl {

	@Autowired
	IWebParametroService webParametroService;

	private static Map<String, Object> map = new HashMap<>();
	private static SecureRandom random = new SecureRandom();

	public EncryptDecryptServiceImpl() {
		// Inicia la tarea programada para crear llaves a la medianoche
		// this.createKeys();
		// scheduleKeyGeneration();
	}

	@PostConstruct
	private void initialize() {
		// Inicia la tarea programada para crear llaves a la medianoche
		this.createKeys();
		scheduleKeyGeneration();
	}

	private void scheduleKeyGeneration() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Obtén la cantidad de segundos hasta la medianoche
		long initialDelay = getSecondsUntilMidnight();

		// Programa la tarea para ejecutarse diariamente a la medianoche
		scheduler.scheduleAtFixedRate(this::createKeys, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
	}

	private long getSecondsUntilMidnight() {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();

		return now.until(midnight, ChronoUnit.SECONDS);
	}

	public void createKeys() {
		try {
			// Genera la semilla aleatoria
			byte[] seed = new byte[20];
			random.nextBytes(seed);
			
			System.out.println("Array de bytes generado: " + Arrays.toString(seed));

			System.out.println("----------------------------------------------------");
			System.out.println(seed);
			System.out.println("----------------------------------------------------");
			
			String seedHex = DatatypeConverter.printHexBinary(seed);
			
			System.out.println("----------------------------------------------------");
			System.out.println(seedHex);
			System.out.println("----------------------------------------------------");
			
			// Asegura que webParametroService se haya inicializado antes de accederlo
			if (webParametroService != null) {
				this.webParametroService.actualizarSemilla(seed);
			} else {
				// Manejo de error: webParametroService es null
				System.out.println("----------------------------------------------------");
				System.out.println("ERROR webParametroService en EncryptDecryptServiceImpl es null");
				System.out.println("----------------------------------------------------");
			}
			
			
			
			System.out.println("PROVEEDOR ACTUAL: ");
			System.out.println(new SecureRandom(seed).getProvider());
			System.out.println("PROVEEDORES: ");
			Provider[] providers = Security.getProviders();
			for(Provider provider: providers) {
				
				System.out.println(provider);
			}
					
			
			// Utiliza la semilla aleatoria para inicializar el generador de claves
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048, new SecureRandom(seed));
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			map.put("publicKey", publicKey);
			map.put("privateKey", privateKey);

			System.out.println("Nuevas llaves generadas.");
			System.out.println("Public key");
			System.out.println(publicKey);
			System.out.println("Private key");
			System.out.println(privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encryptMessage(String plainText) {
		try {
			
			System.out.println("Mensaje a encriptar");
			System.out.println(plainText);
			
			Date fechaHoraActual = new Date();

	        // FORMATEAR LA FECHA Y HORA COMO UNA CADENA
	        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String fechaHoraFormateada = formatoFechaHora.format(fechaHoraActual);
	        
	        plainText = plainText + "," + fechaHoraFormateada;
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
			PublicKey publicKey = (PublicKey) map.get("publicKey");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypt = cipher.doFinal(plainText.getBytes());
			return new String(Base64.getEncoder().encodeToString(encrypt));
		} catch (Exception e) {
			System.out.println("ERROR al ENCRIPTAR el QR desde el servicio de encriptacion");
			return null;
		}

	}

	public String decryptMessage(String encryptedMessgae) {
		try {
			
			System.out.println("Mensaje a desencriptar");
			System.out.println(encryptedMessgae);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
			PrivateKey privateKey = (PrivateKey) map.get("privateKey");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(encryptedMessgae));
			return new String(decrypt);
		} catch (Exception e) {
			System.out.println("ERROR al DESENCRIPTAR el QR desde el servicio de encriptacion");
			e.printStackTrace();
			return null;
		}

	}
}
