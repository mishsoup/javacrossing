package input;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ui.OutputCreator; 

public class OutputCreator {

    private static OutputCreator theOutputCreator;
    private static StringBuffer jsonOutputAry = new StringBuffer();


    public static OutputCreator getTheOutputCreator() { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "getTheOutputCreator");

        if (theOutputCreator == null) {
            theOutputCreator = new OutputCreator();
        }
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "getTheOutputCreator");
        return theOutputCreator;
    }

    public static void addMainStartJSON(String className, String functionCalled) { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "addMainStartJSON");

        String content = "[{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}";
        jsonOutputAry.append(content);
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "addMainStartJSON");
    } 


    public static void addMainEndJSON(String className, String functionCalled) { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "addMainEndJSON");

        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}]");
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "addMainEndJSON");
    } 


    public static void addFuncStartJSON(String className, String functionCalled) { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "addFuncStartJSON");

        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}");
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "addFuncStartJSON");
    } 


    public static void addFuncEndJSON(String className, String functionCalled) { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "addFuncEndJSON");

        jsonOutputAry.append(",{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\"}");
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "addFuncEndJSON");
    } 


    public static String getJsonOutputAry() { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "getJsonOutputAry");

        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "getJsonOutputAry");
        return jsonOutputAry.toString();
    }

    public static void writeJSONFile(String path) { 
        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
        outputCreator.addFuncStartJSON("OutputCreator", "Start_" + "writeJSONFile");

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
        outputCreator.addFuncEndJSON("OutputCreator", "End_" + "writeJSONFile");
    } 

}
