package com.cristianml.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cristianml.services.CategoriaService;

@Controller
@RequestMapping("")
public class JpaController {
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("/jpa-repository")
	public String home() {
		return "jpa_repository/home";
	}
	
	@GetMapping("/jpa-repository/categorias")
	public String categorias(Model model) {
		model.addAttribute("datos", this.categoriaService.listar());
		return "jpa_repository/categorias";
	}
}
