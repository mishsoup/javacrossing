package writer;

import reader.StringFileContent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class StringFileWriter implements FileWriter<String, StringFileContent> {
    private String mainDirPath;

    public void setMainDirPath(String dirPath) {
        mainDirPath = dirPath;
    }

    public void writeFiles(Map<String, StringFileContent> fileContents) {
        // update all files in input project
        fileContents.forEach((k, v) -> writeFile(k, v.getContent()));
        // inject our OutputCreator to the dic of main in the input project
        writeOutPutCreatorToMainDir();
    }

    private void writeFile(String path, String content) {
        OutputStream outPut = null;
        try {
            outPut = new FileOutputStream(new File(path));
            outPut.write(content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: Path " + path);
        } finally {
            try {
                outPut.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to close OutputStream writer.");
            }
        }
    }

    private void writeOutPutCreatorToMainDir() {
        StringBuffer outPutCreatorPath = new StringBuffer();
        String outPutCreator = "OutputCreator.java";
        String originalOutputCreatorPath = "src/parser/OutputCreator.java";

        // inject outputcreator to make new path
        // 1) add path to dic of main 2)add file name .java
        outPutCreatorPath.append(mainDirPath);
        outPutCreatorPath.append("/" + outPutCreator);
        // read outputcreator
        StringFileContent sfc = null;
        try {
            Path path = Paths.get(originalOutputCreatorPath);
            String content1 = Files.readString(path);
            String dirName = mainDirPath.substring(mainDirPath.lastIndexOf("/") + 1);
            StringBuffer contentAddPackage = new StringBuffer();
            contentAddPackage.append("package " + dirName + ";\n" + content1);
            sfc = new StringFileContent(contentAddPackage.toString());
        } catch (IOException e) {
            throw new RuntimeException("Can not inject OutputCreator to directory of the input project.");
        }
        // write it to project
        writeFile(outPutCreatorPath.toString(), sfc.getContent());
    }
}
