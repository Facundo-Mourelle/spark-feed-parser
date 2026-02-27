package namedEntity;

import java.util.*;
import java.util.function.Function;
import java.io.Serializable;

public abstract class TopicClass implements Serializable {
  private String mainTopic;
  private static final Map<String, Function<String, TopicClass>> topicMap = new HashMap<>();

  static {
    // Deportes
    topicMap.put("fútbol", SportTopic::new);
    topicMap.put("básquet", SportTopic::new);
    topicMap.put("tenis", SportTopic::new);
    topicMap.put("fórmula 1", SportTopic::new);

    // Cultura
    topicMap.put("cine", CultureTopic::new);
    topicMap.put("música", CultureTopic::new);
    topicMap.put("Exposición", CultureTopic::new);
    topicMap.put("Recital", CultureTopic::new);
    // Política
    topicMap.put("nacional", PoliticTopic::new);
    topicMap.put("internacional", PoliticTopic::new);
  }



  public TopicClass(String mainTopic) {
    this.mainTopic = mainTopic;
  }

  public String getMainTopic() {
    return mainTopic;
  }

  @Override
  public String toString() {
    return mainTopic;
  }


  public static TopicClass createTopic(String topic) {

    if (topic == null) {
      return new OtherTopic("otros");
    }

    String normalized = topic.trim().toLowerCase();

    // Busca en el map, o por defecto devuelve OtherTopic
    return topicMap.getOrDefault(normalized, t -> new OtherTopic(t)).apply(normalized);

  }
}
