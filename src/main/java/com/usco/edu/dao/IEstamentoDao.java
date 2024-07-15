package com.usco.edu.dao;

import java.util.List;

import com.usco.edu.entities.Estamento;
import com.usco.edu.entities.CarnetDigital;

public interface IEstamentoDao {
	
	public List<Estamento> estamentos(String userdb);
	
	public List<Estamento> carnets(int percodigo);
	
	public List<CarnetDigital> carnetEstamento(int percodigo);

}
