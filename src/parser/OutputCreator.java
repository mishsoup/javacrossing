package parser;


import org.json.JSONArray;
import org.json.JSONObject;

public class OutputCreator {

    private static OutputCreator theOutputCreator;
    private static JSONArray jsonOutputAry = new JSONArray();


    public static OutputCreator getTheOutputCreator() {
        if (theOutputCreator == null) {
            theOutputCreator = new OutputCreator();
        }
        return theOutputCreator;
    }

    public static void addString(String className, String functionCalled) {
        JSONObject newObj = new JSONObject();
        newObj.put(className, functionCalled);
        jsonOutputAry.put(newObj);
    }

}
