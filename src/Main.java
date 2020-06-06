import extractor.Extractor;
import extractor.FileContent;
import extractor.StringExtractor;
import parser.Parser;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        Extractor extractor = new StringExtractor();
        Map<String, FileContent> fileContents = extractor.extract("input");
        Map<String, FileContent> newFileContents = new HashMap<>();
        fileContents.forEach((k, v) -> {
            String newV = parser.parseJavaFile(v.content);
            FileContent newFileContent = new FileContent(newV);
            newFileContents.put(k, newFileContent);
        });

    }
}
