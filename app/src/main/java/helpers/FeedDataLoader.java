package helpers;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import parser.FeedData;
import parser.FeedDataUtils;

public class FeedDataLoader {
	public JavaRDD<FeedData> load(SparkSession spark, String path) {
		return spark.read().textFile(path).javaRDD()
				.filter(line -> line != null && !line.trim().isEmpty())
				.map(FeedDataUtils::fromJsonLine);
	}
}
