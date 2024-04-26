package com.cristianml.services;

import java.util.List;

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
}
