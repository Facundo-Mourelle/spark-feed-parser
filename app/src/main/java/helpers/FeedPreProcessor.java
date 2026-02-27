package helpers;

import java.io.IOException;
import java.util.List;

import parser.FeedData;
import parser.FeedDataUtils;
import parser.GeneralParser;
import parser.SubscriptionParser;

public class FeedPreProcessor {
	public void process(String subscriptionPath, String outputPath) {
		// No podemos trabajar con datos locales asi q armamos el DDR
		// (dataframe para los amigos) luego podemos trabajar haciendo
		// modificaciones al DDR
		GeneralParser<FeedData> subParser = new SubscriptionParser();
		List<FeedData> feeds = subParser.parse(subscriptionPath);
		try {
			FeedDataUtils.guardarFeedsComoJsonLines(feeds, outputPath);
		} catch (IOException e) {
			System.err.println("Error at storing feeds: " + e);
		}
	}
}
