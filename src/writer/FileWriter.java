package writer;

import java.io.*;

public class FileWriter {

    public void writeStringToJavaFile(String path, String content) {
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
