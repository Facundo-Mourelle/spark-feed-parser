package namedEntity;

public class PersonEntity extends NamedEntity {
  private String apellido;  // forma canónica del apellido
  private String titulo;    // título profesional

  public PersonEntity(String name, String apellido, String titulo, int frequency, TopicClass topic) {
    super(name, "PERSON", frequency, topic);
    this.apellido = apellido;
    this.titulo = titulo;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  @Override
  public String toString() {
    return "PersonEntity [name=" + getName() +
      ", apellido=" + apellido +
      ", titulo=" + titulo +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
