package writer;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonWriter {

    public static void createFile(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Failed to create file: " + e.getMessage());
        }
    }

    public static void writeFile(String fileName, JSONObject data) {
        try {
            Files.write(Paths.get(fileName), data.toString().getBytes());
        } catch (IOException e) {
            System.out.println("ERROR - Could not write to file");
        }

    }
}
