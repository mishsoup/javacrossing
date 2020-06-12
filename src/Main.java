import parser.Parser;
import reader.FileReader;
import reader.StringFileContent;
import reader.StringReader;
import writer.StringFileWriter;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
         Parser parser = new Parser();
        FileReader fileReader = new StringReader();
        StringFileWriter stringFileWriter = new StringFileWriter();

        // read file
        Map<String, StringFileContent> fileContents = fileReader.extract("input");

        // parse file
        Map<String, StringFileContent> newFileContents = parser.parse(fileContents);

        // write file
        stringFileWriter.setMainDirPath(parser.getMainDirPath());
        stringFileWriter.writeFiles(newFileContents);

        // debug Point
        System.out.println("END");

    }
}
