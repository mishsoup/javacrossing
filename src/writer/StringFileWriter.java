package writer;

import reader.StringFileContent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class StringFileWriter implements FileWriter<String, StringFileContent> {

    public void writeFiles(Map<String, StringFileContent> fileContents) {
        // update all files in input project
        fileContents.forEach((k, v) -> writeFile(k, v.content));
        // inject our OutputCreator to the dic of main in the input project
        writeOutPutCreatorToMainDic(fileContents);
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

    private void writeOutPutCreatorToMainDic(Map<String, StringFileContent> fileContentMap) {
        StringBuffer outPutCreatorPath = new StringBuffer();
        String outPutCreator = "OutputCreator.java";
        String originalOutputCreatorPath = "src/parser/OutputCreator.java";
        // find the Main path
        StringBuffer mainPath = new StringBuffer();
        fileContentMap.forEach((k, v) -> {
            if (k.contains("Main")) {
                mainPath.append(k);
            }
        });
        // input/moozeek/src/main/java/ui/Main.java
        // get the Main path without Main
        int lastSlashIndex = mainPath.lastIndexOf("/");
        String pathToMainDic = mainPath.substring(0, lastSlashIndex + 1);
        // inject outputcreator to make new path
        // 1) add path to dic of main 2)add file name .java
        outPutCreatorPath.append(pathToMainDic);
        outPutCreatorPath.append(outPutCreator);
        // read outputcreator
        StringFileContent sfc = null;
        try {
            Path path = Paths.get(originalOutputCreatorPath);
            String content1 = Files.readString(path);
            sfc = new StringFileContent(content1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // write it to project
        writeFile(outPutCreatorPath.toString(), sfc.content);
    }
}
