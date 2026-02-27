package parser;
import java.io.Serializable;


public class FeedData implements Serializable{

    private String label;
    private String url;
    private String type;

    FeedData(String label, String url, String type){
        this.label = label;
        this.url = url;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public String getURL() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public void print() {
        System.out.println("Feed: " + label);
        System.out.println("URL: " + url);
        System.out.println("Type: " + type);
    }
}
