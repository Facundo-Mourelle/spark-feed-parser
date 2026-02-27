package parser;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FeedDataUtils {

    /*
     * Guarda cada FeedData como una línea JSON (JSON Lines), compatible con Spark.read().textFile(...).
     */
    public static void guardarFeedsComoJsonLines(List<FeedData> feeds, String archivoDestino) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
            int i  = 0;
            for (FeedData feed : feeds) {
                if(i != 0){
                    writer.newLine();
                }
                i++;
                JSONObject obj = new JSONObject();
                obj.put("label", feed.getLabel());
                obj.put("url", feed.getURL());
                obj.put("type", feed.getType());
                writer.write(obj.toString());
            }
        }
    }

    /*
     * Parsea una línea JSON (obtenida de un RDD de Spark) y devuelve un FeedData.
     */
    public static FeedData fromJsonLine(String jsonLine) {
        JSONObject obj = new JSONObject(jsonLine);
        String label = obj.getString("label");
        String url = obj.getString("url");
        String type = obj.getString("type");
        return new FeedData(label, url, type);
    }
}
