package com.cristianml.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cristianml.models.CategoriaModel;
import com.cristianml.models.ProductoModel;

public interface IProductoRepository extends JpaRepository<ProductoModel, Integer>{
	List<ProductoModel> findAllByCategoriaId(CategoriaModel categoria);
}
