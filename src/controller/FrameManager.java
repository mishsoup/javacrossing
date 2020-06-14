package controller;

import model.Frame;
import model.JavaClass;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FrameManager {
    private List<String> xAxis = new ArrayList<>();
    private List<Integer> yAxis = new ArrayList<>();
    private List<Double> size = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private List<Frame> frames = new ArrayList<>();
    private boolean isScaledOnTime;

    // new list to keep track of which function maps to which index in the text array
    private HashMap<String,Integer> classFuncToTextMap = new HashMap<>();

    private final double SIZE_SCALE_FACTOR = 0.2;
    private final double DEFAULT_SIZE = 10;

    public FrameManager(boolean isScaledOnTime) {
        this.isScaledOnTime = isScaledOnTime;
    }

    public void addDataPoint(JavaClass jClass, String text, String funcName) {
        xAxis.add(jClass.getName());
        yAxis.add(jClass.getAvailableYPoint());
        size.add(DEFAULT_SIZE);
        colors.add(jClass.getColor());
        texts.add(text);
        classFuncToTextMap.put(jClass.getName()+funcName, texts.size() - 1);
    }

    public void updateDataPoint(JavaClass jClass, String text, String funcName) {
        int textIndex = classFuncToTextMap.get(jClass.getName()+funcName);
        texts.set(textIndex, text);
    }

    public void upDateTextWithTime(JavaClass jClass, String funcName, long timeUsed) {
        int textIndex = classFuncToTextMap.get(jClass.getName()+funcName);
        String txt = texts.get(textIndex);
        if (txt.contains("ACCUMULATED")) {
            int pos = txt.lastIndexOf('<');
            String newtxt = txt.substring(0, pos) + "<br>ACCUMULATED TIME :"+timeUsed+"ms";
            texts.set(textIndex, newtxt);
        } else {
            texts.set(textIndex, txt+"<br>ACCUMULATED TIME: "+timeUsed+"ms");
        }
    }

    public void scaleDataPoint(int index) {
        double scaledSize = this.size.get(index) + SIZE_SCALE_FACTOR;
        scaledSize = Math.round(scaledSize * 10) / 10.0;
        this.size.set(index, scaledSize);
    }

    public void scaleDataPointTime(int index, long totalTime) {
        // default 10
        double spendTime = (double) totalTime;
        double scaledSize;
        // 10
        if (spendTime == 0) {
            scaledSize = DEFAULT_SIZE;
            // 10 - 20
        } else if (spendTime < 100) {
            scaledSize = DEFAULT_SIZE + spendTime / 10;
            // 20 - 40
        } else if (spendTime < 1000) {
            scaledSize = DEFAULT_SIZE * 2 + spendTime / 45;
            // 40 - 100
        } else if (spendTime < 10000) {
            scaledSize = DEFAULT_SIZE * 4 + spendTime / 167;
        } else {
            scaledSize = DEFAULT_SIZE * 10 + spendTime / 440;
        }
        this.size.set(index, scaledSize);
    }

    public void saveFrame() {
        Frame frame = new Frame(xAxis, yAxis, size, colors, texts);
        this.frames.add(frame);
    }


    public void saveFramesToFile(JSONObject jo) {
        JSONArray framesJSONArray = framesToJSONArray();
        String graphName;
        if (isScaledOnTime) {
            graphName = "spendTime";
        } else {
            graphName = "numberCall";
        }
        jo.put(graphName, framesJSONArray);
    }

    private JSONArray framesToJSONArray() {
        JSONArray jsonArray;
        List<JSONObject> jsonArrayElements = new ArrayList<>();
        for (int idx = 0; idx < frames.size(); idx++) {
            JSONObject frameJson = frameToJson(idx);
            jsonArrayElements.add(frameJson);
            System.out.println(frameJson.toString());
        }
        jsonArray = new JSONArray(jsonArrayElements);
        return jsonArray;
    }

    private JSONObject frameToJson(int index) {
        int frameNumber = index + 1;
        Frame frame = frames.get(index);
        String frameName = "frame" + frameNumber;
        JSONObject frameJson = frame.toJson(frameName);
        return frameJson;
    }
}
