package com.cristianml.controllers;

import java.util.HashMap;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cristianml.models.CategoriaModel;
import com.cristianml.models.ProductoModel;
import com.cristianml.services.CategoriaService;
import com.cristianml.services.ProductoService;
import com.cristianml.utilities.Utilities;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/jpa-repository")
public class JpaController {
	
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private ProductoService productoService;

	@Value("${cristian.valores.ruta.url_base}")
	String ruta_upload;
	
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
	
	// Editar categoría
	@GetMapping("/categorias/edit/{id}")
	public String categorias_edit(@PathVariable("id") Integer id, Model model) {
		CategoriaModel categoria = categoriaService.buscarPorId(id);
		model.addAttribute("categoria", categoria);
		return "/jpa_repository/categorias_edit";
	}
	
	@PostMapping("/categorias/edit/{id}")
	public String categorias_editar_post(@Valid CategoriaModel categoria, BindingResult result, Model model
			, @PathVariable Integer id, RedirectAttributes flash) {
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
			return "/jpa_repository/categorias_edit";
			}
		
		// Comprobamos el slug
		String slug = Utilities.getSlug(categoria.getNombre());
		categoria.setSlug(slug);
		categoriaService.save(categoria);
		flash.addFlashAttribute("clase", "success");
		flash.addFlashAttribute("mensaje", "El registro fue editado exitosamente.");
		return "redirect:/jpa-repository/categorias/edit/"+id;
		

	}

	// Delete categoría
	@GetMapping("/categorias/delete/{id}")
	public String categorias_delete(@PathVariable("id") Integer id,  RedirectAttributes flash) {
		// Lo cerramos en un try catch
		try {
			this.categoriaService.delete(id);
			flash.addFlashAttribute("clase", "success");
			flash.addFlashAttribute("mensaje", "Se eliminó el registro exitosamente.");
			return "redirect:/jpa-repository/categorias";
		} catch (Exception e) {
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", "No se pudo eliminar el registro, intentelo más tarde.");
			return "redirect:/jpa-repository/categorias";
		}
	}
	
	// ================================== PRODUCTOS ===============================================================
	// Listar Productos
	@GetMapping("/productos")
	public String productos(Model model) {
		model.addAttribute("datos" , this.productoService.listar());
		model.addAttribute("categorias" , categoriaService.listar());
		return "/jpa_repository/productos";
	}
	
	// ========================================== CAMPOS GENÉRICOS ========================================
	@ModelAttribute
	public void setGenerics(Model model) {
		model.addAttribute("ruta_upload", this.ruta_upload);
	}
	
	
	
}
