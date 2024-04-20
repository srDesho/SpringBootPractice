package com.cristianml.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cristianml.models.ProductoModel;

public interface IProductoRepository extends JpaRepository<ProductoModel, Integer>{

}
