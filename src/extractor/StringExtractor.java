package extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringExtractor implements Extractor<String, FileContent> {
    // key: filePath;     value: fileContent in String form
    Map<String, FileContent> fileContents = new HashMap<>();

    public Map<String, FileContent> extract(String inputPath) {
        // produce contents map
        produceFileContentsMap(inputPath);
        return fileContents;
    }

    private void produceFileContentsMap(String inputPath) {
        List<String> paths = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(inputPath))) {
            paths = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".java")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String filePath: paths) {
            try {
                Path path = Paths.get(filePath);
                String content1 = Files.readString(path);
                FileContent fileContent = new FileContent(content1);
                fileContents.put(filePath, fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
