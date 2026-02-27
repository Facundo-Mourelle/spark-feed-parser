package helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import feed.Article;
import httpRequest.HttpRequester;
import httpRequest.HttpRequesterReddit;
import httpRequest.HttpRequesterRss;
import parser.FeedData;
import parser.GeneralParser;
import parser.RssParser;
import parser.RedditParser;

public class FeedProcessor {
	public JavaRDD<Article> extractArticles(JavaRDD<FeedData> feedDataRDD) {
		// Obtener y parsear feeds (como antes)
		// Hacemos un map con una "lambda"
		// para transformar FeedData en Lista de Articulos
		// notar q el traspaso es uno a uno entre Lista de
		// Articulos y FeedData pero no todas las listas
		// tienen el mismo #de elementos.
		return feedDataRDD.map(feed -> {
			String type = feed.getType();
			String url = feed.getURL();

			// Validaciones claramente hechas por mi
			if (type == null || url == null) {
				System.err.println("Warning: Feed con type o URL nulo, saltando...");
				return new ArrayList<Article>();
			}

			GeneralParser parser;
			HttpRequester requester;

			try {
				switch (type.toLowerCase()) {
					case "rss":
						requester = new HttpRequesterRss();
						parser = new RssParser();
						break;
					case "reddit":
						requester = new HttpRequesterReddit();
						parser = new RedditParser();
						break;
					default:
						System.err.println(
								"Warning: Tipo de parser desconocido: "
										+ type);
						return new ArrayList<Article>();
				}
			} catch (Exception e) {
				System.err.println("Error creando parser: " + e.getMessage());
				return new ArrayList<Article>();
			}

			try {

				// ******** REQUEST *******
				String content_feed = requester.getFeed(url);

				// mas validaciones hechas por mi 0_0
				if (content_feed == null || content_feed.trim().isEmpty()) {
					System.err.println(
							"Warning: Contenido del feed vacío para URL: "
									+ url);
					return new ArrayList<Article>();
				}

				// ****** PARSE DE LA REQUEST *******
				List<Article> article_list = parser.parse(content_feed);
				return article_list != null ? article_list : new ArrayList<Article>();

			} catch (Exception e) {
				System.err.println(
						"Error procesando feed " + url + ": " + e.getMessage());
				return new ArrayList<Article>();
			}
		})

				// Convertimos en RDD plano de artículos individuales
				// por la anotacion previa.
				// Funcionamietno: FlatMap aplana mapeando :)
				// ej [a1,a2.a3] [b1,b2] [c1] -> [a1,a2,a3,b1,b2,c1]
				// necesita q le devuelvas un iterador ya que es con
				// lo que efectivamente hace el aplanamiento
				// aplicamos directamente sobre el mapeo
				.filter(list -> list != null && !list.isEmpty())
				.flatMap(List::iterator);

	}
}
