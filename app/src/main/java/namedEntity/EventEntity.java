package namedEntity;

public class EventEntity extends NamedEntity {
  private String fecha;

  public EventEntity(String name, String fecha, int frequency, TopicClass topic) {
    super(name, "EVENT", frequency, topic);
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  @Override
  public String toString() {
    return "EventEntity [name=" + getName() +
      ", fecha=" + fecha +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
