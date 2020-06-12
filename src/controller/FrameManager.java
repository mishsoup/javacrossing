package controller;

import model.Frame;
import model.JavaClass;
import org.json.JSONArray;
import org.json.JSONObject;
import writer.JsonWriter;

import java.util.ArrayList;
import java.util.List;

public class FrameManager {
    private List<String> xAxis = new ArrayList<>();
    private List<Integer> yAxis = new ArrayList<>();
    private List<Double> size = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<String> texts = new ArrayList<>();

    private List<Frame> frames = new ArrayList<>();

    private final double SIZE_SCALE_FACTOR = 0.2;
    private final double DEFAULT_SIZE = 10;

    public void addDataPoint(JavaClass jClass, String text) {
        xAxis.add(jClass.getName());
        yAxis.add(jClass.getAvailableYPoint());
        size.add(DEFAULT_SIZE);
        colors.add(jClass.getColor());
        texts.add(text);
    }

    public void scaleDataPoint(int index) {
        double scaledSize = this.size.get(index) + SIZE_SCALE_FACTOR;
        this.size.set(index, scaledSize);
    }

    public void saveFrame() {
        Frame frame = new Frame(xAxis, yAxis, size, colors, texts);
        this.frames.add(frame);
    }


    public void saveFramesToFile(String fileName) {
        JSONArray framesJSONArray = framesToJSONArray();
        JSONObject framesObject = new JSONObject();

        framesObject.put("results", framesJSONArray);
        JsonWriter.writeFile(fileName, framesObject);
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
