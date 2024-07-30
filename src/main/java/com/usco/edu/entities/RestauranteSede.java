package com.usco.edu.entities;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestauranteSede implements Serializable {
	
	private int uaaCodigo;
	private String uaaNombre;
	private int sedeCodigo;
	private String sedeNombre;
	
	private static final long serialVersionUID = 1L;
	
}
