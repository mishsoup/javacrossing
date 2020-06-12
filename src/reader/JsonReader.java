package reader;

import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonReader implements FileReader<String, JSONArray> {

    Map<String, JSONArray> jsonArray= new HashMap<>();
    public static final String RESULT_KEY = "results";

    @Override
    public Map<String, JSONArray> extract(String inputPath) {
        readFile(inputPath);
        return jsonArray;
    }

    private void readFile(String inputPath) {
        try {
            String jsonString = Files.readString(Paths.get(inputPath));
            JSONArray json = new JSONArray(jsonString);
            jsonArray.put(RESULT_KEY, json);
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Failed to read file: " + e.getMessage());
        }

    }
}
