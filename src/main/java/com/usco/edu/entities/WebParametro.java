package com.usco.edu.entities;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebParametro implements Serializable {
	
	private int codigo;
	private String webNonbre;
	private String webValor;
	private String webDescripcion;
	private int estado;
	
	private static final long serialVersionUID = 1L;
}
