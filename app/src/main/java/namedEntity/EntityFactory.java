package namedEntity;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.FileInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class EntityFactory {

    private static final Map<String, NamedEntity> dictionary = new HashMap<>();

    public static void start(String jsonPath) {
        try (InputStream is = new FileInputStream(jsonPath)) {
            JSONArray arr = new JSONArray(new JSONTokener(is));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                NamedEntity ne = buildFromJson(obj);
                dictionary.put(ne.name, ne);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static NamedEntity buildFromJson(JSONObject obj) {
        String name = obj.getString("name");
        String category = obj.getString("category");
        int frequency = obj.optInt("frequency", 1);
        String topic_str = obj.optString("topic", "Unknown");
        TopicClass topic = TopicClass.createTopic(topic_str);

        switch (category.toUpperCase()) {
        case "PERSON":
            String apellido = obj.optString("apellido", "");
            String titulo = obj.optString("titulo", "");
            return new PersonEntity(name, apellido, titulo, frequency, topic);
        case "DATE":
            return new DateEntity(name, frequency, topic);
        case "EVENT":
            String fecha = obj.optString("fecha", "");
            return new EventEntity(name, fecha, frequency, topic);
        case "ORGANIZATION":
            String tipoOrg = obj.optString("tipo_organizacion", "");
            int miembros = obj.optInt("numero_miembros", 0);
            return new OrganizationEntity(name, tipoOrg, miembros, frequency, topic);
        case "PLACE":
            String pais = obj.optString("pais", "");
            String ciudad = obj.optString("ciudad", "");
            String direccion = obj.optString("direccion", "");
            return new PlaceEntity(name, pais, ciudad, direccion, frequency, topic);
        case "PRODUCT":
            String tipo = obj.optString("tipo", "");
            String productor = obj.optString("productor", "");
            return new ProductEntity(name, tipo, productor, frequency, topic);
        default:
            return new OtherEntity(name, frequency, topic);
        }
    }

    // si lo encuentre en el diccionario toma la frequency y topic del diccionario
    // sino si toma la de los argumentos
    public static NamedEntity build(String name, int frequency, TopicClass topic) {
        NamedEntity completeNamedEntity;
        completeNamedEntity = dictionary.get(name);
        if(completeNamedEntity != null){
            return completeNamedEntity;
        }
        completeNamedEntity = infereEntityTipe(name);
        if(topic != null){completeNamedEntity.setTopic(topic);}
        completeNamedEntity.setFrequency(frequency);
        return completeNamedEntity;
    }


    private static NamedEntity infereEntityTipe(String name) {
        // Normalizar nombre
        String lower = name.toLowerCase();
        TopicClass topic = TopicClass.createTopic("Unknown");
        int frequency = 1;

        // Organización
        if (lower.matches(".*(inc\\.?|corp\\.?|s\\.a\\.?|llc|team)$")) {
            return new OrganizationEntity(name, "empresa", 0, frequency, topic);
        }

        // Persona (títulos comunes al inicio)
        if (lower.matches("^(dr|mr|ms|mrs|prof)\\.?\\s+\\w+")) {
            return new PersonEntity(name, "Apellido", "Título", frequency, topic);
        }

        // Fecha
        if (lower.matches("\\d{1,2}/\\d{1,2}/\\d{2,4}") ||
            lower.matches(".*(january|february|march|april|may|june|july|august|september|october|november|december).*")) {
            return new DateEntity(name, frequency, topic);
        }

        // Ciudad
        if (lower.matches(".*(city|town)$")) {
            return new PlaceEntity(name, "UnknownCountry", name, "UnknownAddress", frequency, topic);
        }

        // País
        if (lower.matches(".*(land|nia|stan)$")) {
            return new PlaceEntity(name, name, null, null, frequency, topic);
        }

        // Evento
        if (lower.matches(".*(conference|summit|world cup|olympics|festival).*")) {
            return new EventEntity(name, "unknownDate", frequency, topic);
        }

        // Producto
        if (lower.matches(".*(model|series|version|pro|max)$")) {
            return new ProductEntity(name, "comercial", "desconocido", frequency, topic);
        }

        // Default (no se pudo inferir)
        return new OtherEntity(name, frequency, topic);
    }

    public static void main(String[] args) {
        // Ruta al archivo JSON de entidades
        String jsonPath = "TestDictionary.json"; // Asegurate de tener este archivo en el mismo directorio

        // Cargar entidades desde archivo
        EntityFactory.start(jsonPath);

        // Caso 1: entidad que sí existe en el diccionario
        NamedEntity e1 = EntityFactory.build("Messi", 3, TopicClass.createTopic("fútbol"));
        System.out.println("Entidad desde JSON: " + e1.toString());

        // Caso 2: entidad que no existe, debe inferirse como ORGANIZATION
        NamedEntity e2 = EntityFactory.build("TechCorp", 5, TopicClass.createTopic("musica"));
        System.out.println("Entidad inferida: " + e2.toString());

        // Caso 3: entidad que no existe, debe inferirse como PRODUCT
        NamedEntity e3 = EntityFactory.build("iPhone Pro Max", 10, TopicClass.createTopic("fotografía"));
        System.out.println("Entidad inferida: " + e3.toString());

        // Caso 4: entidad desconocida
        NamedEntity e4 = EntityFactory.build("NonsenseName", 1, TopicClass.createTopic("otros"));
        System.out.println("Entidad desconocida: " + e4.toString());
    }

}
