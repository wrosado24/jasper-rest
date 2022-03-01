package com.nivutech.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nivutech.entity.Persona;
import com.nivutech.repository.PersonaRepository;

@Service
public class PersonaService {
	
	@Autowired
	private PersonaRepository personaRepository;
	
	public List<Persona> list(){
		return personaRepository.findAll();
	}
	
	public void reportePdf() {
		
	}
	
	public void reporteXls() {
		
	}

}
