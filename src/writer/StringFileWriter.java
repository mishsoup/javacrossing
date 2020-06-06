package writer;

import reader.StringFileContent;

import java.io.*;
import java.util.Map;

public class StringFileWriter implements FileWriter<String, StringFileContent> {

    public void writeFiles(Map<String, StringFileContent> fileContents) {
        fileContents.forEach((k, v) -> writeFile(k, v.content));
    }

    private void writeFile(String path, String content) {
        OutputStream outPut = null;
        try {
            outPut = new FileOutputStream(new File(path));
            outPut.write(content.getBytes(), 0, content.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outPut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
