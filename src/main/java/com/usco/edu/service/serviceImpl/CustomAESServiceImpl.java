package com.usco.edu.service.serviceImpl;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.usco.edu.service.IWebParametroService;
import java.security.Key;
import java.util.Base64;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
@EnableScheduling
public class CustomAESServiceImpl {

	@Autowired
	IWebParametroService webParametroService;

	private static final String ALGORITHM = "AES";
	private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private Key key;

	@PostConstruct
	private void initialize() {
		this.generateKey();
		scheduleKeyGeneration();

		System.out.println("CUSTOM KEY GENERATED!");
		System.out.println("---------------------------------------------------------");
		System.out.println("KEY");
		System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
	}

	public void generateKey() {
		try {
			long seed = byteArrayToLong(this.generateRandomSeed());
			SecureRandom secureRandom = new SecureRandom();
			secureRandom.setSeed(seed);
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
			keyGen.init(128, secureRandom); // Tamaño de clave de 128 bits
			SecretKey newKey = keyGen.generateKey();

			// Usa la clave generada directamente sin modificaciones
			key = new SecretKeySpec(newKey.getEncoded(), ALGORITHM);

			// Imprime la clave en Base64 para verificación
			System.out.println("Generated Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BigInteger encrypt(BigInteger message) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// Convertir BigInteger a byte[]
			byte[] messageBytes = message.toByteArray();

			// Encriptar el byte[]
			byte[] encryptedBytes = cipher.doFinal(messageBytes);

			// Convertir el byte[] en BigInteger
			return new BigInteger(1, encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BigInteger decrypt(BigInteger encryptedMessage) {
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key);

			// Convertir BigInteger a byte[]
			byte[] encryptedBytes = encryptedMessage.toByteArray();

			// Desencriptar el byte[]
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			// Convertir byte[] a BigInteger
			return new BigInteger(1, decryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private void scheduleKeyGeneration() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Obtén la cantidad de segundos hasta la medianoche
		long initialDelay = getSecondsUntilMidnight();

		// Programa la tarea para ejecutarse diariamente a la medianoche
		scheduler.scheduleAtFixedRate(this::generateKey, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
	}

	private long getSecondsUntilMidnight() {
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();

		return now.until(midnight, ChronoUnit.SECONDS);
	}

	private byte[] generateRandomSeed() {
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

		return seedBytes;
	}

	private static long byteArrayToLong(byte[] bytes) {
		long value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value = (value << 8) + (bytes[i] & 0xff);
		}
		return value;
	}
}