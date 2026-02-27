package namedEntity;

public class OtherTopic extends TopicClass {
  private String comment;

  public OtherTopic(String comment) {
    super("OTHER");
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return super.toString() + " -> " + comment;
  }
}
