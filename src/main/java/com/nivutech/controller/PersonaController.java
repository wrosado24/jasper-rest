package com.nivutech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nivutech.entity.Persona;
import com.nivutech.repository.PersonaRepository;


@CrossOrigin("*")
@RestController
@RequestMapping("persona")
public class PersonaController {

	@Autowired
	private PersonaRepository personaRepository;
	
	@GetMapping("/listar")
	public List<Persona> listar(){
		return personaRepository.findAll();
	}
}
