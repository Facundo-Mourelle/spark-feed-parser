package namedEntity;

public class CultureTopic extends TopicClass {
  private String cultureType; // cine, música, etc.

  public CultureTopic(String cultureType) {
    super("CULTURE");
    this.cultureType = cultureType;
  }

  public String getCultureType() {
    return cultureType;
  }

  @Override
  public String toString() {
    return super.toString() + " -> " + cultureType;
}
}
