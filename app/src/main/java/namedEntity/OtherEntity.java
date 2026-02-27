package namedEntity;

public class OtherEntity extends NamedEntity {

  public OtherEntity(String name, int frequency, TopicClass topic) {
    super(name, "OTHER", frequency, topic);
  }

  @Override
  public String toString() {
    return "OtherEntity [name=" + getName() +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
