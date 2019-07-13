package com.srmasset.thcepdetails.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Address implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String estado;
	
	@Getter
	@Setter
	private String cidade;
	
	@Getter
	@Setter
	private String bairro;
	
	@Getter
	@Setter
	private String logradouro;
}
