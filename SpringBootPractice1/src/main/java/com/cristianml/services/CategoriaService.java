package com.cristianml.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.cristianml.models.CategoriaModel;
import com.cristianml.repositories.ICategoriaRepository;

@Service
@Primary
public class CategoriaService {
	
	// Inyectamos nuestro repositorio
	
	@Autowired
	private ICategoriaRepository repository;
	
	// Listar
	public List<CategoriaModel> listar() {
		return repository.findAll();
	}
	
	// Agregar categoria
	public void save(CategoriaModel categoria) {
		repository.save(categoria);
	}
	
	// Buscar por slug
	public boolean buscarPorSlug(String slug) {
		if( repository.existsBySlug(slug) ) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
	
	
}
