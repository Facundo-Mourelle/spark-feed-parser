package namedEntity;

public class OrganizationEntity extends NamedEntity {
  private String TypeOfOrganization;
  private int membersCount;

  public OrganizationEntity(String name, String too, int membersCount, int frequency, TopicClass topic) {
    super(name, "ORGANIZATION", frequency, topic);
    this.TypeOfOrganization = too;
    this.membersCount = membersCount;
  }

  public String getTypeOforganization() {
    return TypeOfOrganization;
  }

  public void setTypeOfOrganization(String too) {
    this.TypeOfOrganization = too;
  }

  public int getMembersCount() {
    return membersCount;
  }

  public void setTitulo(int count) {
    this.membersCount = count;
  }

  @Override
  public String toString() {
    return "OrganizationEntity [name=" + getName() +
      ", TypeOfOrganization=" + TypeOfOrganization +
      ", MembersCount=" + membersCount +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
