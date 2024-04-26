package com.cristianml.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Esta clase va a ser para configurar el método addResourceHandlers para indicar las rutas personalizadas
// para los registros estáticos en los que vamos a trabajar(ejemplo upload de archivos como las imágenes).
// Nos servirá para agregar las rutas externas y se almacenen en alguna carpeta que enrutemos.

// Con esto también se puede configurar spring security, podemos configurar las rutas endpoint donde
// se va a construir un cliente de integración

// agregamos la siguiente anotación, y a la clase le implementamos WebMvcConfigurer para poder indicar la ruta

@Configuration
public class Configuracion implements WebMvcConfigurer {
	
	// Creamos una variable que va a recibir la ruta de la carpeta del ordenador que seleccionamos, desde
	// el application.properties con la anotación @Value
	@Value("${cristian.valores.ruta}")
	private String ruta_upload;
	
	// Generamos el método addResourceHandlers(ResourceHandlerRegistry registry) con el generador de métodos del IDE

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		// WebMvcConfigurer.super.addResourceHandlers(registry); // Comentamos esto por ahora no nos sirve
		
		// agregamos el nombre de la carpeta y doble ** para especificar que usará todos lo que tenga dentro
		registry.addResourceHandler("/upload/**")
		// Agregamos la ruta escribiendolo en duro
		// .addResourceLocations("file: D:\\Backend 2024\\StsProject\\SpringBoot-UdemyCesar\\upload\\");
		// .addResourceLocations("file:///C:/upload/");
		// Agregamos la ruta con la variable
		.addResourceLocations("file: " + this.ruta_upload);
	}
	
}
