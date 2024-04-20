package com.cristianml.models;


public class ProductoModel {

	private Integer id;
	private String nombre;
	private String slug;
	private Integer precio;
	private String descripcion;
	private String foto;
	
	private CategoriaModel categoriaId;

	public ProductoModel() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Integer getPrecio() {
		return precio;
	}

	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public CategoriaModel getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(CategoriaModel categoriaId) {
		this.categoriaId = categoriaId;
	}
	
}
