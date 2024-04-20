package com.cristianml.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cristianml.models.CategoriaModel;

public interface ICategoriaRepository extends JpaRepository<CategoriaModel, Integer> {

}
