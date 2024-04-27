package com.cristianml.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.cristianml.models.ProductoModel;
import com.cristianml.repositories.IProductoRepository;

@Service
@Primary
public class ProductoService {

	@Autowired
	private IProductoRepository repository;
	
	
	public List<ProductoModel> listar() {
		return repository.findAll();
	}
	
	// Guardar Producto
	public void save(ProductoModel producto) {
		repository.save(producto);
	}
	
	// Delete por id
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
	
	// Obtener por id
	public ProductoModel findById(Integer id) {
		Optional<ProductoModel> optional = repository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
		
	}
}
