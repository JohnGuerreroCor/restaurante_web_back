package com.usco.edu.service.serviceImpl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.usco.edu.service.IWebParametroService;

@Service
@EnableScheduling
public class CustomRSAServiceImpl {

	@Autowired
	IWebParametroService webParametroService;

	private BigInteger p; // Número primo p
	private BigInteger q; // Número primo q
	private BigInteger n; // n = p * q
	private BigInteger phi; // Función phi de Euler de n
	private BigInteger e; // Clave pública
	private BigInteger d; // Clave privada

	@PostConstruct
	private void initialize() {
		// Inicia la tarea programada para crear llaves a la medianoche
		this.generateKeys();
		scheduleKeyGeneration();

		System.out.println("CUSTOM KEYS GENERATED!");
		System.out.println("---------------------------------------------------------");
		System.out.println("PUBLIC KEY");
		System.out.println(e);
		System.out.println("PRIVATE KEY");
		System.out.println(d);
	}

	// Método para generar claves
	public void generateKeys() {
		int bitLength = 1024;
		long seed = this.generateRandomSeed();
		Random rnd = new Random(seed);// aca va como argumento la semilla
		p = BigInteger.probablePrime(bitLength, rnd);
		q = BigInteger.probablePrime(bitLength, rnd);
		n = p.multiply(q);
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

		// Elegir e: debe ser un número primo relativo a phi
		do {
			e = BigInteger.probablePrime(bitLength / 2, rnd); // Usar un bitLength más pequeño para e
		} while (e.gcd(phi).compareTo(BigInteger.ONE) != 0 && e.compareTo(phi) < 0);

		// Calcular d, el inverso multiplicativo de e módulo phi
		d = e.modInverse(phi);
	}

	// Método para encriptar un mensaje
	public BigInteger encrypt(BigInteger message) {
		return message.modPow(e, n);
	}

	// Método para desencriptar un mensaje encriptado
	public BigInteger decrypt(BigInteger encryptedMessage) {
		return encryptedMessage.modPow(d, n);
	}

	private void scheduleKeyGeneration() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Obtén la cantidad de segundos hasta la medianoche
		long initialDelay = getSecondsUntilMidnight();

		// Programa la tarea para ejecutarse diariamente a la medianoche
		scheduler.scheduleAtFixedRate(this::generateKeys, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
	}

	private long getSecondsUntilMidnight() {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();

		return now.until(midnight, ChronoUnit.SECONDS);
	}

	public long generateRandomSeed() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] seedBytes = new byte[8]; // 8 bytes para un long
		secureRandom.nextBytes(seedBytes);

		// Asegura que webParametroService se haya inicializado antes de accederlo
		if (webParametroService != null) {
			this.webParametroService.actualizarSemilla(seedBytes);
		} else {
			// Manejo de error: webParametroService es null
			System.out.println("----------------------------------------------------");
			System.out.println("ERROR webParametroService en EncryptDecryptServiceImpl es null");
			System.out.println("----------------------------------------------------");
		}

		return byteArrayToLong(seedBytes);
	}

	private static long byteArrayToLong(byte[] bytes) {
		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value = (value << 8) + (bytes[i] & 0xff);
		}
		return value;
	}
}
