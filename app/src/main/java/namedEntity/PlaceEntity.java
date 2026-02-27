package namedEntity;

public class PlaceEntity extends NamedEntity {
  private String pais;
  private String ciudad;
  private String direccion;

  public PlaceEntity(String name, String pais, String ciudad, String direccion, int frequency, TopicClass topic) {
    super(name, "PLACE", frequency, topic);
    this.pais = pais;
    this.ciudad = ciudad;
    this.direccion = direccion;
  }

  public String getPais() {
    return pais;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public String getCiudad() {
    return ciudad;
  }

  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  @Override
  public String toString() {
    return "PlaceEntity [name=" + getName() +
      ", pais=" + pais +
      ", ciudad=" + ciudad +
      ", direccion=" + direccion +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
