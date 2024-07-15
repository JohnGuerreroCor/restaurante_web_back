package com.usco.edu.entities;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Qr implements Serializable {

	private String mensajeEncriptado;

	private static final long serialVersionUID = 1L;
}
