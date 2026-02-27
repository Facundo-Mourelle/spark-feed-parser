package namedEntity;

public class SportTopic extends TopicClass {
  private String sportType;

  public SportTopic(String sportType) {
    super("SPORTS");
    this.sportType = sportType;
  }

  public String getSportType() {
    return sportType;
  }

  @Override
    public String toString() {
      return super.toString() + " -> " + sportType;
  }
}
