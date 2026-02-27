package httpRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

//Para testing, quitar public a
public abstract class HttpRequester {

	protected enum Mtype {
		UNDEFINED, XML, JSON
	};

	protected abstract Mtype getFormat();

	public String getFeed(String urlFeed) {

		String feed = null;

		String format_accepted = null;
		switch (getFormat()) {
			case XML:
				format_accepted = "application/rss+xml";
				break;
			case JSON:
				format_accepted = "application/json";
				break;
			default:
				format_accepted = "*/*";
		}

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(urlFeed))
				.header("Accept", format_accepted)
				.header("User-Agent", "FeedReader/1.0")
				.timeout(Duration.ofSeconds(20))
				.GET()
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				throw new Exception("Error en la conexión HTTP: " + response.statusCode());
			}
			feed = response.body();
		} catch (Exception e) {
			System.out.println("Error al intentar establecer conexión");
		}

		return feed;
	}

	public static void main(String[] args) throws Exception {

		// Just to sort-of testing, it does not execute
		String xmlurl = "https://rss.nytimes.com/services/xml/rss/nyt/Business.xml";
		String content = null;
		HttpRequester requester = new HttpRequesterRss();

		try {
			content = requester.getFeed(xmlurl);
		} catch (Exception e) {
			System.out.println("F panas, no se logro la request");
			throw new Exception("request not done");
		}
		// Define la ruta y el nombre del archivo de texto
		File textFile = new File("output.txt");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
			// Escribe el contenido en el archivo
			writer.write(content);
			System.out.println("Archivo de texto creado con éxito.");
		} catch (IOException e) {
			// Manejo de excepciones
			System.err.println("Hubo un error al escribir el archivo de texto: " + e.getMessage());
		}

	}

}
