package com.cristianml.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.cristianml.repositories.ICategoriaRepository;

@Service
@Primary
public class CategoriaService {
	
	// Inyectamos nuestro repositorio
	
	@Autowired
	private ICategoriaRepository repository;
}
