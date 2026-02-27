package namedEntity;

public class DateEntity extends NamedEntity {

  public DateEntity(String name, int frequency, TopicClass topic) {
    super(name, "DATE", frequency, topic);
  }

  @Override
  public String toString() {
    return "DateEntity [name=" + getName() +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
