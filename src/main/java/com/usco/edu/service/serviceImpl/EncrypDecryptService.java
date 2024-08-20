package com.usco.edu.service.serviceImpl;

import java.math.BigInteger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class EncrypDecryptService {

	public BigInteger encrypt(BigInteger message) {
		try {
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BigInteger decrypt(BigInteger encryptedMessage) {
		try {
			return encryptedMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}