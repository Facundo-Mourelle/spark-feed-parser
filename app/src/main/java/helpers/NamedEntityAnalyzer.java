package helpers;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import feed.Article;
import feed.Feed;
import namedEntity.NamedEntity;
import namedEntity.heuristic.Heuristic;
import scala.Tuple2;

public class NamedEntityAnalyzer {
	private List<Tuple2<String, Integer>> ListEntity;
	private int totalArticles = 0;
	private PrintWriter writer;
	private PrintWriter ne_writer;

	public List<Tuple2<String, Integer>> analyze(JavaRDD<Article> articles, Heuristic heuristic, int max_size, String outputPath) {
		try {
			// Inicializar el writer para el archivo
			this.writer = new PrintWriter(new FileWriter(outputPath));
			JavaRDD<Article> withEntities = computeNamedEntities(articles, heuristic, max_size);
			JavaPairRDD<String, Integer> entityCounts = countNamedEntities(withEntities);
			this.ListEntity = sortEntitiesByFrequency(entityCounts);
			this.totalArticles = (int) articles.count(); // Para estadísticas
			printSortedEntities();
			printFeed(withEntities);

		} catch (IOException e) {
			System.err.println("Error al escribir al archivo: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Cerrar el writer
			if (this.writer != null) {
				this.writer.close();
			}
		}

		return this.ListEntity;
	}

	private JavaRDD<Article> computeNamedEntities(JavaRDD<Article> articles, Heuristic heuristic, int max_size) {
		return articles.map(article -> {
			article.computeNamedEntities(heuristic, max_size);
			return article;
		});
	}

	private JavaPairRDD<String, Integer> countNamedEntities(JavaRDD<Article> articlesWithEntities) {
		return articlesWithEntities
				.flatMap(article -> {
					List<NamedEntity> list = article.getNamedEntityList();
					return list != null ? list.iterator() : new ArrayList<NamedEntity>().iterator();
				})
				.mapToPair(entity -> new Tuple2<>(entity.getName(), 1))
				.reduceByKey(Integer::sum);
	}

	private List<Tuple2<String, Integer>> sortEntitiesByFrequency(JavaPairRDD<String, Integer> entityCounts) {
		return entityCounts
				.mapToPair(tuple -> new Tuple2<>(tuple._2(), tuple._1()))
				.sortByKey(false)
				.mapToPair(tuple -> new Tuple2<>(tuple._2(), tuple._1()))
				.collect();
	}

	public void printSortedEntities() {
		writer.println("\n*** ESTADÍSTICAS DE ENTIDADES NOMBRADAS ***");
		writer.println("Total de entidades únicas encontradas: " + this.ListEntity.size());
		writer.println("Total de artículos procesados: " + this.totalArticles);
		writer.println("\n*** TOP 20 ENTIDADES MÁS FRECUENTES ***");
		this.ListEntity.stream()
				.limit(20)
				.forEach(tuple -> writer.println(
						String.format("%-30s: %d apariciones", tuple._1(), tuple._2())));
		if (!this.ListEntity.isEmpty()) {
			int totalMentions = this.ListEntity.stream()
					.mapToInt(tuple -> tuple._2())
					.sum();
			writer.println("\n*** ESTADÍSTICAS GENERALES ***");
			writer.println("Total de menciones de entidades: " + totalMentions);
			writer.println("Promedio de menciones por entidad: " +
					String.format("%.2f", (double) totalMentions / this.ListEntity.size()));
			writer.println("Entidad más mencionada: " +
					this.ListEntity.get(0)._1() + " (" + this.ListEntity.get(0)._2() + " veces)");
		}
		writer.flush(); // Asegurar que se escriba inmediatamente
	}

	private void printFeed(JavaRDD<Article> articlesWithEntities) {
		List<Article> finalArticles = articlesWithEntities.collect();
		Feed processedFeed = new Feed("My Custom Feed with Named Entities");
		processedFeed.setArticleList(finalArticles);

		// Redirigir la salida del prettyPrint al archivo
		System.out.println("\n*** FEED PROCESADO ***");
		processedFeed.prettyPrint(); // Asumiendo que prettyPrint puede aceptar PrintWriter
	}
}
