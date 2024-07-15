package com.usco.edu.entities;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarnetDigital implements Serializable {
	
	private int codigo;
	private String estamento;
	private int uaa;
	private String usuario;

	private static final long serialVersionUID = 1L;
	
}
