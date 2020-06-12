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

    // SINCE main is the begin and end of the process, so it will have [ and ] for begin and end
    // WHICH is diff with normal function
    public static void addMainStartJSON(String className, String functionCalled) {
        String content = "[" + makeJSONObject(className, functionCalled);
        jsonOutputAry.append(content);
    }

    public static void addMainEndJSON(String className, String functionCalled) {
        jsonOutputAry.append(","+ makeJSONObject(className, functionCalled) +"]");
    }

    public static void addFuncJSON(String className, String functionCalled) {
        jsonOutputAry.append(","+makeJSONObject(className, functionCalled));
    }

    private static String makeJSONObject(String className, String functionCalled) {
        return "{\"classname\":\"" + className + "\",\"function\":\"" + functionCalled + "\",\"time\":\""+ getRecentTime()+"\"}";
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

    private static String getRecentTime() {
    Long time = System.currentTimeMillis();
        return time.toString();
    }
}
