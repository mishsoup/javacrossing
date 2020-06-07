import parser.Parser;
import reader.FileReader;
import reader.StringFileContent;
import reader.StringReader;
import writer.StringFileWriter;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        FileReader fileReader = new StringReader();
        StringFileWriter stringFileWriter = new StringFileWriter();

        // read file
        Map<String, StringFileContent> fileContents = fileReader.extract("input");
        Map<String, StringFileContent> newFileContents = new HashMap<>();

        // parse file
        fileContents.forEach((k, v) -> {
            String newV = parser.parseJavaFile(v.content);
            StringFileContent newStringFileContent = new StringFileContent(newV);
            newFileContents.put(k, newStringFileContent);
        });

        // write file
//        stringFileWriter.writeFiles(newFileContents);

        // debug Point
        System.out.println("END");
    }
}
