package namedEntity;

import java.io.Serializable;
/*Esta clase modela la nocion de entidad nombrada*/

public class NamedEntity implements Serializable {
	String name;
	String category; 
	int frequency;
	TopicClass topic;
	
	public NamedEntity(String name, String category, int frequency, TopicClass topic) {
		super();
		this.name = name;
		this.category = category;
		this.frequency = frequency;
		this.topic = topic;
	}

	public TopicClass getTopic() {
		return topic;
	}

	public void setTopic(TopicClass topic) {
		this.topic = topic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return name;
	}

	public void setCategory(String name) {
		this.name = name;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public void incFrequency() {
		this.frequency++;
	}

	@Override
	public String toString() {
		return "ObjectNamedEntity [name=" + name + ", frequency=" + frequency + "]";
	}
	public void prettyPrint(){
		System.out.println(this.toString());
	}
	
	
}



