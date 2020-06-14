package controller;

import model.JavaClass;
import model.JavaFunctions;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Color;
import util.TextGenerator;
import writer.JsonWriter;

import java.util.HashMap;
import java.util.Map;


public class PlotlyController {


    private FrameManager fm;
    private FrameManager fmOnTime;
    private Color colorManager;
    private JSONArray json;

    private final String JSON_CLASSNAME = "classname";
    private final String JSON_FXNNAME = "function";
    private final String JSON_FXNTIME = "time";

    private Map<String, JavaClass> javaClassesMap = new HashMap<>();
    private int currentIndex = 0;
    private int numberOfUpdates = 0;
    private int frameNumberThreshold;

    public PlotlyController(JSONArray jsonArray, int frameNumberThreshold) {
        json = jsonArray;
        fm = new FrameManager(false);
        fmOnTime = new FrameManager(true);
        colorManager = new Color();
        this.frameNumberThreshold = frameNumberThreshold;
    }

    public void drawPlotly() {
        for (Object jsonObject : this.json) {
            JSONObject jsn  = (JSONObject) jsonObject;
            // information extracted  from json
            String javaClassName = jsn.getString(JSON_CLASSNAME);
            String javaFxnName = jsn.getString(JSON_FXNNAME);
            String parsedJavaFxnName = parseFunctionString(javaFxnName);
            long javaFxnTime = Long.parseLong(jsn.getString(JSON_FXNTIME));
            JavaClass javaClass = getOrAddJavaClass(javaClassName);

            numberOfUpdates++;

            if (isEndingFunction(javaFxnName)) {
                handleEndingCall(javaClass, parsedJavaFxnName, javaFxnTime);
                fmOnTime.saveFrame();
                continue;
            } else if (javaClass.isFunctionMember(parsedJavaFxnName)) {
                javaClass.updateFunction(parsedJavaFxnName, javaFxnTime);
                String hoverText = generateText(javaClass, parsedJavaFxnName);

                fm.updateDataPoint(javaClass,hoverText, parsedJavaFxnName);
                fm.scaleDataPoint(javaClass.getFunction(parsedJavaFxnName).getIndex());

                fmOnTime.updateDataPoint(javaClass, hoverText, parsedJavaFxnName);
            } else {
                // create function entry
                javaClass.addFunction(parsedJavaFxnName, currentIndex, javaFxnTime);
                currentIndex++;
                // generate text when hovered
                String hoverText = generateText(javaClass, parsedJavaFxnName);

                // update each frame managers
                fm.addDataPoint(javaClass, hoverText, parsedJavaFxnName);
                fmOnTime.addDataPoint(javaClass, hoverText, parsedJavaFxnName);
            }

            if (numberOfUpdates >= frameNumberThreshold) {
                fm.saveFrame();
                numberOfUpdates = 0;
            }
        }
        fm.saveFrame();
    }

    public void savePlotlyFramesToFile(String fileName) {
        JSONObject framesObject = new JSONObject();
        fm.saveFramesToFile(framesObject);
        fmOnTime.saveFramesToFile(framesObject);
        JsonWriter.writeFile(fileName, framesObject);
    }

    private void handleEndingCall(JavaClass javaClass, String javaFxnName, long javaFxnTime) {
        JavaFunctions jf = javaClass.getFunction(javaFxnName);
        jf.decreaseStartEndIndex();
        if (jf.isFunEnd()) {
            long entryTime = javaClass.getFunction(javaFxnName).getEntryTime();
            long timeUsed = javaFxnTime - entryTime;
            long totalTime = javaClass.getFunction(javaFxnName).increaseTotalTime(timeUsed);
            fm.upDateTextWithTime(javaClass, javaFxnName, totalTime);
            fmOnTime.upDateTextWithTime(javaClass, javaFxnName, totalTime);
            fmOnTime.scaleDataPointTime(javaClass.getFunction(javaFxnName).getIndex(), totalTime);
        }
    }

    private boolean isEndingFunction(String functionName) {
        return functionName.contains("End");
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
