package com.cristianml.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cristianml.models.CategoriaModel;
import com.cristianml.services.CategoriaService;
import com.cristianml.utilities.Utilities;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/jpa-repository")
public class JpaController {
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("")
	public String home() {
		return "jpa_repository/home";
	}
	
	@GetMapping("/categorias")
	public String categorias(Model model) {
		model.addAttribute("datos", this.categoriaService.listar());
		return "jpa_repository/categorias";
	}
	
	// Crear categoría
	@GetMapping("/categorias/add")
	public String crear_categoria(Model model) {
		model.addAttribute("categoria", new CategoriaModel());
		return "/jpa_repository/categorias_add";
	}
	
	@PostMapping("/categorias/add")
	public String crear_categoria(@Valid CategoriaModel categoria, BindingResult result, RedirectAttributes flash
			, Model model) {
		// Validamos nuestro dato
		if(result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors()
			.forEach( err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			
			model.addAttribute("errores", errores);
			model.addAttribute("categoria", categoria);
			return "/jpa_repository/categorias_add";
			}
		// Verificamos si nuestro slug ya existe en la base de datos
		String slug = Utilities.getSlug(categoria.getNombre());
		if(this.categoriaService.buscarPorSlug(slug) == false) {
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", "El registro ya existe en la base de datos.");
			return "redirect:/jpa-repository/categorias/add";
		} else {
			categoria.setSlug(slug);
			this.categoriaService.save(categoria);
			flash.addFlashAttribute("clase", "success");
			flash.addFlashAttribute("mensaje", "Registro agregado satisfactoriamente.");
			return "redirect:/jpa-repository/categorias/add";
		}
	}
	
	
	
	
	
}
