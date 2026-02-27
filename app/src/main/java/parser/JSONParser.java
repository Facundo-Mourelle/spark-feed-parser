package parser;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import feed.Article;

public abstract class JSONParser extends GeneralParser<Article> {
	protected JSONArray jsonRoot;

	public JSONParser() {
	}

	public JSONArray parseToJSONArray(String input) {
		Object parsed = new JSONTokener(input).nextValue();
		if (!(parsed instanceof JSONArray)) {
			throw new IllegalArgumentException("Expected root-level JSON array");
		}
		this.jsonRoot = (JSONArray) parsed;
		return (JSONArray) parsed;
	}

	@Override
	public abstract List<Article> parse(String input);

	// Getter of jsonRoot
	public JSONArray getJsonRoot() {
		return this.jsonRoot;
	}

	/**
	 * Creates a new JSON object with the specified fields from the source
	 * 
	 * @param source The source JSON object
	 * @param fields Array of field names to extract
	 * @return A new JSONObject with the specified fields
	 */
	public static JSONObject extractFields(JSONObject source, String[] fields) {
		JSONObject result = new JSONObject();
		for (String field : fields) {
			if (source.has(field)) {
				result.put(field, source.get(field));
			}
		}
		return result;
	}
}
