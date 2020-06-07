package ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputCreator {

    private static OutputCreator theOutputCreator;
    private static StringBuffer jsonOutputAry = new StringBuffer();


    public static OutputCreator getTheOutputCreator() {
        if (theOutputCreator == null) {
            theOutputCreator = new OutputCreator();
        }
        return theOutputCreator;
    }

    public static void addMainStartJSON(String className, String functionCalled) {
        String content = "[{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}";
        jsonOutputAry.append(content);
    }

    public static void addMainEndJSON(String className, String functionCalled) {
        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}]");
    }

    public static void addFuncStartJSON(String className, String functionCalled) {
        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}");
    }

    public static void addFuncEndJSON(String className, String functionCalled) {
        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}");
    }

    public static String getJsonOutputAry() {
        return jsonOutputAry.toString();
    }

    public static void writeJSONFile(String path) {
        OutputStream outPut = null;
        String content = getJsonOutputAry();
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
