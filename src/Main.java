import reader.FileReader;
import reader.FileContentString;
import reader.StringReader;
import parser.Parser;
import writer.FileWriter;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        FileReader fileReader = new StringReader();
        FileWriter fileWriter = new FileWriter();

        Map<String, FileContentString> fileContents = fileReader.extract("input");
        Map<String, FileContentString> newFileContents = new HashMap<>();
        fileContents.forEach((k, v) -> {
            String newV = parser.parseJavaFile(v.content);
            FileContentString newFileContentString = new FileContentString(newV);
            newFileContents.put(k, newFileContentString);
        });

        newFileContents.forEach((k, v) -> {
            fileWriter.writeStringToJavaFile(k, v.content);
        });

        // debug Point
        System.out.println("END");
    }
}
