package com.cristianml.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cristianml.models.CategoriaModel;
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
	
	// Listar descendente
	public List<ProductoModel> listarDescending() {
		return repository.findAll(Sort.by("id").descending());
	}
	
	// Guardar Producto
	public void save(ProductoModel producto) {
		repository.save(producto);
	}
	
	// Delete por id
	public void deleteById(Integer id) {
		repository.deleteById(id);
	}
	
	// Listar por categoría
	public List<ProductoModel> buscarPorCategoria(CategoriaModel categoria) {
		return repository.findAllByCategoriaId(categoria);
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
