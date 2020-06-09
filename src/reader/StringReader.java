package reader;

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

public class StringReader implements FileReader<String, StringFileContent> {
    // key: filePath;     value: fileContent in String form (wrap in StringFileContent class since some java.file to be
    // string is too large so we can not put it directly to Map value)
    Map<String, StringFileContent> fileContents = new HashMap<>();

    public Map<String, StringFileContent> extract(String inputPath) {
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
        if (paths.size() == 0) {
            throw new RuntimeException("Can not find any java file in input directory.");
        }
        for (String filePath: paths) {
            try {
                Path path = Paths.get(filePath);
                String content1 = Files.readString(path);
                StringFileContent stringFileContent = new StringFileContent(content1);
                fileContents.put(filePath, stringFileContent);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file: Path " + filePath);
            }
        }
    }

}
