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
import javax.crypto.spec.SecretKeySpec;
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
    private static final int MAX_ENCRYPTED_LENGTH = 30;
    private static final int KEY_LENGTH = 16; // RC4 key length

    @PostConstruct
    private void initialize() {
        createKeys();
        scheduleKeyGeneration();
    }

    private void scheduleKeyGeneration() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long initialDelay = getSecondsUntilMidnight();
        scheduler.scheduleAtFixedRate(this::createKeys, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
    }

    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return now.until(midnight, ChronoUnit.SECONDS);
    }

    public void createKeys() {
        try {
            byte[] seed = new byte[KEY_LENGTH];
            random.nextBytes(seed);
            SecretKeySpec secretKey = new SecretKeySpec(seed, "RC4");
            map.put("secretKey", secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptMessage(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec secretKey = (SecretKeySpec) map.get("secretKey");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            String encoded = Base64.getEncoder().encodeToString(encryptedBytes);
            return encoded.length() > MAX_ENCRYPTED_LENGTH ? encoded.substring(0, MAX_ENCRYPTED_LENGTH) : encoded;
        } catch (Exception e) {
            System.out.println("ERROR al ENCRIPTAR el mensaje desde el servicio de encriptacion");
            e.printStackTrace();
            return null;
        }
    }

    public String decryptMessage(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec secretKey = (SecretKeySpec) map.get("secretKey");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.out.println("ERROR al DESENCRIPTAR el mensaje desde el servicio de encriptacion");
            e.printStackTrace();
            return null;
        }
    }
}