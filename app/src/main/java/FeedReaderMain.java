import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.IOException;
import scala.Tuple2;
// Spark
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.SparkSession;
// Parser
import parser.SubscriptionParser;
import parser.GeneralParser;
import parser.RssParser;
import parser.RedditParser;
import parser.FeedData;
import parser.FeedDataUtils;
// Feed
import feed.Article;
import feed.Feed;
// HTTP
import httpRequest.HttpRequester;
import httpRequest.HttpRequesterRss;
import httpRequest.HttpRequesterReddit;
// Named Entity
import namedEntity.EntityFactory;
import namedEntity.heuristic.Heuristic;
import namedEntity.heuristic.QuickHeuristic;
import namedEntity.NamedEntity;
// Helpers
import helpers.FeedPreProcessor;
import helpers.FeedProcessor;
import helpers.FeedDataLoader;
import helpers.NamedEntityAnalyzer;
import helpers.SparkContextFactory;

public class FeedReaderMain {
	private static final String DICTIONARY_PATH = "data/config/EntitiesDictionary.json";
	private static final int MAX_SIZE_TOKEN = 2;
	private static final String SUBSCRIPTION_PATH = "data/config/subscriptions.json";
	private static final String FEEDDATA_PATH = "data/config/FeedDataFile.json";
	private static final String NE_STATS_FILE = "data/config/ne_stats.txt";

	private static void printHelp() {
		System.out.println("Please, call this program in correct way: FeedReader [-ne]");
	}

	public static void main(String[] args) {
		System.out.println("************* FeedReader version 1.0 *************");

		if (args.length < 2) {
			// Replace here with custom heuristic
			Heuristic heuristica = new QuickHeuristic();
			EntityFactory.start(DICTIONARY_PATH);

			new FeedPreProcessor().process(SUBSCRIPTION_PATH, FEEDDATA_PATH);
			SparkSession spark = SparkContextFactory.createSession("FeedReader");

			// Damos contexto y parseamos el FeedDataFile
			JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

			// MAGIA!: modificamos el DDR, ahora es un dataframe de FeedData
			JavaRDD<FeedData> feeds = new FeedDataLoader().load(spark, FEEDDATA_PATH);

			JavaRDD<Article> articles = new FeedProcessor().extractArticles(feeds);

			if (args.length == 1) {
				System.out.println("\n*** PROCESAMIENTO DISTRIBUIDO DE ENTIDADES NOMBRADAS ***");

				new NamedEntityAnalyzer().analyze(articles, heuristica, MAX_SIZE_TOKEN, NE_STATS_FILE);

			} else {
				// Sin procesamiento de entidades nombradas
				List<Article> simpleArticles = articles.collect();
				Feed processed_feed = new Feed("My Custom Feed");
				processed_feed.setArticleList(simpleArticles);
				processed_feed.prettyPrint();
			}

			sc.close();

		} else {
			printHelp();
		}
	}
}
