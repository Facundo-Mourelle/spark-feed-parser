package namedEntity;

public class PoliticTopic extends TopicClass {
  private String scope;

  public PoliticTopic(String scope) {
    super("POLITICS");
    this.scope = scope;
  }

  public String getScope() {
    return scope;
  }

  @Override
  public String toString() {
    return super.toString() + " -> " + scope;
  }
}