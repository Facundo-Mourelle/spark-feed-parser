package parser;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import feed.Article;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser<Article> {
  protected List<Article> articleList;

  private static final String[] XML_FIELDS = {
      "title",
      "link",
      "description",
      "pubDate"
  };

  public List<Article> getArticles() {
    return this.articleList;
  }

  /** Converts a file input into a proper web-format document */
  @Override
  public List<Article> parse(String file) {

    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document xmldoc = builder.parse(new InputSource(new StringReader(file)));


      return this.parseToFeed(xmldoc);

    } catch (ParserConfigurationException e) {
      throw new RuntimeException("Parser configuration error", e);

    } catch (IOException e) {
      throw new RuntimeException("Dom Builder error", e);

    } catch (SAXException e) {
      throw new RuntimeException("Dom Builder error", e);
    }
  }

  public List<Article> parseToFeed(Document doc) {
    List<Article> articles = new ArrayList<>();

    NodeList items = doc.getElementsByTagName("item");
    for (int i = 0; i < items.getLength(); i++) {
      Node node = items.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;

        String title = getTagValue(XML_FIELDS[0], element);
        String description = getTagValue(XML_FIELDS[1], element);
        String link = getTagValue(XML_FIELDS[2], element);
        String pubDateRaw = getTagValue(XML_FIELDS[3], element);

        Date pubDate = parseDate(pubDateRaw);

        Article article = new Article(title, description, pubDate, link);
        articles.add(article);
      }
    }

    return articles;
  }

  // Helper method to retrieve elements from tag
  private String getTagValue(String tag, Element element) {
    NodeList nodes = element.getElementsByTagName(tag);
    if (nodes.getLength() > 0 && nodes.item(0).hasChildNodes()) {
      return nodes.item(0).getFirstChild().getNodeValue();
    }
    return "";
  }

  // Helper method to parse Date
  private Date parseDate(String pubDateRaw) {
    try {
      // e.g., "Sat, 18 May 2024 10:00:00 GMT"
      SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
      return format.parse(pubDateRaw);
    } catch (ParseException e) {
      e.printStackTrace();
      return new Date(); // just to avoid breaking everything
    }
  }

  protected Map<String, Object> extractFields(Element source, String[] fields) {
    Map<String, Object> result = new HashMap<>();
    for (String field : fields) {
      NodeList nodes = source.getElementsByTagName(field);
      if (nodes.getLength() > 0 && nodes.item(0).hasChildNodes()) {
        String value = nodes.item(0).getFirstChild().getNodeValue();
        result.put(field, value);
      }
    }
    return result;
  }
}
