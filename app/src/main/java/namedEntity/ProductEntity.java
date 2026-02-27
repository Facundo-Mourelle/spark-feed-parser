package namedEntity;

public class ProductEntity extends NamedEntity {
  private String tipo;
  private String productor;

  public ProductEntity(String name, String tipo, String productor, int frequency, TopicClass topic) {
    super(name, "PRODUCT", frequency, topic);
    this.tipo = tipo;
    this.productor = productor;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getProductor() {
    return productor;
  }

  public void setProductor(String productor) {
    this.productor = productor;
  }

  @Override
  public String toString() {
    return "ProductEntity [name=" + getName() +
      ", tipo=" + tipo +
      ", productor=" + productor +
      ", frecuencia=" + getFrequency() +
      ", topic=" + getTopic() +
      "]";
  }
}
