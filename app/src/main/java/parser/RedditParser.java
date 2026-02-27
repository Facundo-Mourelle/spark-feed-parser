package parser;

import feed.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RedditParser extends JSONParser {
	List<Article> articleList;

	private static final String[] REDDIT_FIELDS = {
			"title",
			"selftext",
			"url",
			"created_utc"
	};

	public List<Article> getArticles() {
		return this.articleList;
	}

	@Override
	public List<Article> parse(String input) {
		JSONObject json = new JSONObject(input);
		this.parseAndStoreArticles(json);
		return this.articleList;
	}

	public void parseAndStoreArticles(JSONObject redditResponse) {
		JSONArray children = redditResponse
				.getJSONObject("data")
				.getJSONArray("children");

		List<Article> articles = new ArrayList<>();

		for (int i = 0; i < children.length(); i++) {
			JSONObject postData = children.getJSONObject(i).getJSONObject("data");
			Article article = parseToArticle(postData);
			articles.add(article);
		}

		this.articleList = articles;
	}

	// Note: postData must be the final data{} JSONObject
	public Article parseToArticle(JSONObject postData) {
		JSONObject fields = extractFields(postData, REDDIT_FIELDS);

		String title = fields.optString("title", "");
		String text = fields.optString("selftext", "");
		String url = fields.optString("url", "");
		long timestamp = fields.optLong("created_utc", 0L);
		Date date = new Date(timestamp * 1000); // Reddit timestamps are in seconds

		return new Article(title, text, date, url);
	}

}
