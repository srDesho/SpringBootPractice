package com.cristianml.utilities;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utilities {

	// slug
		// En un proyecto Java, un "slug" es una cadena de texto que se utiliza para identificar de forma única
		// un recurso o entidad dentro de una aplicación web. Generalmente, los slugs se utilizan en URL 
		// amigables para mejorar la legibilidad y la SEO (optimización para motores de búsqueda) de los enlaces.
		
		// Esto nos va a servir para formatear y construir el slug que vamos a necesitar
		private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
		private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
		private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

		// Método para el slug
		// Convierte la cadena de texto en un slug
		public static String getSlug(String input) {
			String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
			String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
			String slug = NONLATIN.matcher(normalized).replaceAll("");
			slug = EDGESDHASHES.matcher(slug).replaceAll("");
			return slug.toLowerCase(Locale.ENGLISH);
		}
	
}
