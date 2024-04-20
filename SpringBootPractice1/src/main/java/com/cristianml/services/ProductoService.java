package com.cristianml.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.cristianml.repositories.IProductoRepository;

@Service
@Primary
public class ProductoService {

	@Autowired
	private IProductoRepository repository;
	
}
