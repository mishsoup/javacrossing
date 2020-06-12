package controller;

import model.Frame;
import model.JavaClass;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Color;
import util.TextGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlotlyController {


    private FrameManager fm;
    private Color colorManager;
    private JSONArray json;

    private final String JSON_CLASSNAME = "classname";
    private final String JSON_FXNNAME = "function";

    private Map<String, JavaClass> javaClassesMap = new HashMap<>();
    private List<Frame> frames = new ArrayList<>();

    private int currentIndex = 0;

    public PlotlyController(JSONArray jsonArray) {
        json = jsonArray;
        fm = new FrameManager();
        colorManager = new Color();
    }

    public void drawPlotly() {
        for (Object jsonObject : this.json) {
            // System.out.println(jsonObject.toString());
            JSONObject jsn  = (JSONObject) jsonObject;

            String javaClassName = jsn.getString(JSON_CLASSNAME);
            String javaFxnName = jsn.getString(JSON_FXNNAME);

            JavaClass javaClass = getOrAddJavaClass(javaClassName);

            // ignore ending call
            if (isEndingFunction(javaFxnName)) {
                continue;
            }

            javaFxnName = parseFunctionString(javaFxnName);
            if (javaClass.isFunctionMember(javaFxnName)) {
                javaClass.updateFunction(javaFxnName);
                fm.scaleDataPoint(javaClass.getFunction(javaFxnName).getIndex());
            } else {
                // create function entry
                javaClass.addFunction(javaFxnName, currentIndex);
                currentIndex++;

                // generate text when hovered
                String hoverText =generateText(javaClass, javaFxnName);
                fm.addDataPoint(javaClass,hoverText);
            }
            fm.saveFrame();
        }
    }

    public void savePlotlyFramesToFile(String fileName) {
        fm.saveFramesToFile(fileName);
    }

    private boolean isEndingFunction(String functionName) {
        return functionName.startsWith("End");
    }

    private String parseFunctionString(String functionName) {
        String[] functionSplit = functionName.split("_");
        return functionSplit[1];
    }

    private String generateText(JavaClass jc, String fxnName) {
        return TextGenerator.generateText(jc, jc.getFunction(fxnName));
    }

    private JavaClass getOrAddJavaClass(String className) {
        if (!javaClassesMap.containsKey(className)) {
            javaClassesMap.put(className, new JavaClass(className, colorManager.getColor().toString()));
        }

        JavaClass jc = javaClassesMap.get(className);
        return jc;
    }



}