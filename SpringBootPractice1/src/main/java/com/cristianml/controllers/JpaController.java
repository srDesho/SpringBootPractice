package com.cristianml.controllers;

import java.io.File;
import java.net.http.HttpClient.Redirect;
import java.util.HashMap;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cristianml.models.CategoriaModel;
import com.cristianml.models.ProductoModel;
import com.cristianml.repositories.ICategoriaRepository;
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
	
	@Value("${cristian.valores.ruta}")
	private String ruta_upload_server;
	
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
		model.addAttribute("datos" , this.productoService.listarDescending());
		model.addAttribute("categorias" , categoriaService.listar());
		return "/jpa_repository/productos";
	}
	
	// Crear producto
	@GetMapping("/productos/add")
	public String productos_add(Model model) {
		model.addAttribute("producto", new ProductoModel());
		model.addAttribute("categorias", this.categoriaService.listar());
		return "jpa_repository/productos_add";
	}
	
	@PostMapping("/productos/add")
	public String productos_add_post(@Valid ProductoModel producto, BindingResult result, RedirectAttributes flash
			, @RequestParam("archivoImagen") MultipartFile multiPart, Model model ) {
		// Validamos nuestro dato
		if(result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors()
			.forEach( err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			
			model.addAttribute("errores", errores);
			model.addAttribute("producto", producto);
			return "/jpa_repository/productos_add";
			}
		// Validamos la imágen la imágen
		if(multiPart.isEmpty()) {
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", "El archivo para la imágen es obligatorio, debe ser JPG|JPEG|PNG");
			model.addAttribute("producto", producto);
			return "redirect:/jpa-repository/productos/add";
		} 
		
		if (!multiPart.isEmpty()) {
			// Creamos el nombre de la imágen y a su vez se guarda la imágen gracias a nuestra utilidad creada en el paquete Utilitie
			String nombreImagen = Utilities.guardarArchivo(multiPart, this.ruta_upload_server + "producto/");
			// Verificamos si es una imágen aceptada
			if(nombreImagen == "no") {
				flash.addFlashAttribute("clase", "danger");
				flash.addFlashAttribute("mensaje", "El archivo para la imágen no es válido, debe ser JPG|JPEG|PNG");
				model.addAttribute("producto", producto);
				return "redirect:/jpa-repository/productos/add";
			} 
			if (nombreImagen != null) {
				producto.setFoto(nombreImagen);
			}
		}
		// Creamos nuestro slug automático
		String slug = Utilities.getSlug(producto.getNombre());
		producto.setSlug(slug);
		productoService.save(producto);
		flash.addFlashAttribute("clase", "success");
		flash.addFlashAttribute("mensaje", "Se creó el registro exitosamente.");
		return "redirect:/jpa-repository/productos/add";
	}
	
	// Editar producto
	@GetMapping("/productos/edit/{id}")
	public String productos_edit(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("producto", productoService.findById(id));
		model.addAttribute("categorias", this.categoriaService.listar());
		return "/jpa_repository/productos_edit";
	}
	
	@PostMapping("/productos/edit/{id}")
	public String productos_edit_post(@Valid ProductoModel producto, BindingResult result, RedirectAttributes flash
			,@PathVariable("id") Integer id , @RequestParam("archivoImagen") MultipartFile multiPart, Model model ) {
		// Validamos nuestro dato
		if(result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors()
			.forEach( err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});
			
			model.addAttribute("errores", errores);
			model.addAttribute("producto", producto);
			return "/jpa_repository/productos_edit";
			}
		
		if (!multiPart.isEmpty()) {
			// Creamos el nombre de la imágen y a su vez se guarda la imágen gracias a nuestra utilidad creada en el paquete Utilitie
			String nombreImagen = Utilities.guardarArchivo(multiPart, this.ruta_upload_server + "producto/");
			// Verificamos si es una imágen aceptada
			if(nombreImagen == "no") {
				flash.addFlashAttribute("clase", "danger");
				flash.addFlashAttribute("mensaje", "El archivo para la imágen no es válido, debe ser JPG|JPEG|PNG");
				model.addAttribute("producto", producto);
				return "redirect:/jpa-repository/productos/edit/"+id;
			} 
			if (nombreImagen != null) {
				producto.setFoto(nombreImagen);
			}
		}
		// Creamos nuestro slug automático
		String slug = Utilities.getSlug(producto.getNombre());
		producto.setSlug(slug);
		productoService.save(producto);
		flash.addFlashAttribute("clase", "success");
		flash.addFlashAttribute("mensaje", "Se editó el registro exitosamente.");
		return "redirect:/jpa-repository/productos/edit/"+id;
	}
	
	// Delete Producto
	@GetMapping("/productos/delete/{id}")
	public String productos_delete(@PathVariable("id") Integer id, RedirectAttributes flash) {
		ProductoModel producto = productoService.findById(id);
		try {
			// obtenemos la imágen desde el servidor para eliminarlo del mismo mientra eliminamos el registro
			File objImagen = new File(ruta_upload_server+"/producto/"+producto.getFoto());
			if( objImagen.delete() ) {
				this.productoService.deleteById(id);
				flash.addFlashAttribute("clase", "success");
				flash.addFlashAttribute("mensaje", "Se eliminó el registro exitosamente.");
			} else {
				flash.addFlashAttribute("clase", "danger");
				flash.addFlashAttribute("mensaje", "No se pudo eliminar el registro, intentelo más tarde.");
			}	
		} catch (Exception e) {
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", "No se pudo eliminar el registro, intentelo más tarde.");
			return "redirect:/jpa-repository/productos";
		}
		return "redirect:/jpa-repository/productos";
	}
	
	// BUSCAR PRODUCTOS POR CATEGORIAS
	@GetMapping("/productos/categorias/{id}")
	public String productos_categorias(@PathVariable Integer id, Model model) {
		CategoriaModel categoria = categoriaService.buscarPorId(id);
		// Verificamos si la categoría existe, para saber que no sea una inyección malicia
		if (categoria == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "página no encontrada");
		}
		model.addAttribute("categoria", categoria);
		model.addAttribute("datos", this.productoService.buscarPorCategoria(categoria));
		return "/jpa_repository/productos_categorias";
	}
	
	
	
	// ========================================== CAMPOS GENÉRICOS ========================================
	@ModelAttribute
	public void setGenerics(Model model) {
		model.addAttribute("ruta_upload", this.ruta_upload);
	}
	
	
	
}
