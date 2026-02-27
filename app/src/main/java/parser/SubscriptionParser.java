package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
/*
 * Esta clase implementa el parser del  archivo de suscripcion (json)
 * Leer https://www.w3docs.com/snippets/java/how-to-parse-json-in-java.html
 * */

public class SubscriptionParser extends GeneralParser<FeedData> {
	private List<FeedData> requestList;

	private static final String[] SUBSCRIPTION_FIELDS = {
			"url",
			"urlParams",
			"urlType",
			"download"
	};

	public SubscriptionParser() {
		this.requestList = new ArrayList<>();
	}

	@Override
	public List<FeedData> parse(String input) {
		try {
			Path path = Paths.get(input);
			String processed_input = Files.readString(path);
			JSONArray parsedArray = (JSONArray) new JSONTokener(processed_input).nextValue();
			if (!(parsedArray instanceof JSONArray)) {
				throw new IllegalArgumentException("Expected root-level JSON array");
			}
			this.buildRequestsFrom(parsedArray);
		} catch (IOException e) {
			throw new RuntimeException("Error reading file", e);
		}
		return this.requestList;

	}

	public List<FeedData> getRequestList() {
		return this.requestList;
	}

	/** Builds the proper request from each JSON object of the array */
	public void buildRequestsFrom(JSONArray jsonArray) {
		List<FeedData> feedList = new ArrayList<>();
		int size = jsonArray.length();
		for (int i = 0; i < size; i++) {
			JSONObject read = jsonArray.getJSONObject(i);
			JSONObject subscription = JSONParser.extractFields(read, SUBSCRIPTION_FIELDS);

			String url = subscription.getString(SUBSCRIPTION_FIELDS[0]);
			JSONArray params = subscription.getJSONArray(SUBSCRIPTION_FIELDS[1]);
			String type = subscription.getString(SUBSCRIPTION_FIELDS[2]);
			//String download = subscription.getString(SUBSCRIPTION_FIELDS[3]);

			List<FeedData> requests = buildURL(url, params, type);
			feedList.addAll(requests);
		}
		this.requestList = feedList;
	}

	// Helper method to build URLs
	private List<FeedData> buildURL(String url, JSONArray urlParams, String urlType) {
		List<FeedData> fullData = new ArrayList<FeedData>();
		for (int i = 0; i < urlParams.length(); i++) {
			String param = urlParams.getString(i);
			/* format busca %type y reemplaza con argumentos dados */
			String fullUrl = String.format(url, param);
			FeedData data = new FeedData(param, fullUrl, urlType);
			fullData.add(data);
		}
		return fullData;
	}

}
