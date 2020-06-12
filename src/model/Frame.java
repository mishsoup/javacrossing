package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private List<String> xAxis;
    private List<Integer> yAxis;
    private List<Double> size;
    private List<String> colors;
    private List<String> texts;

    public Frame(List<String> xAxis, List<Integer> yAxis, List<Double> size, List<String> colors, List<String> texts) {
        this.xAxis = new ArrayList<>(xAxis);
        this.yAxis = new ArrayList<>(yAxis);
        this.size = new ArrayList<>(size);
        this.colors = new ArrayList<>(colors);
        this.texts = new ArrayList<>(texts);
    }


    public JSONObject toJson(String frameName) {
        JSONObject jsonObject = new JSONObject();
        JSONArray dataArray;

        JSONObject dataObject = getDataObject();
        List<JSONObject> dataList = new ArrayList<>();
        dataList.add(dataObject);
        dataArray = new JSONArray(dataList);

        jsonObject.put("data", dataArray);
        jsonObject.put("name", frameName);
        return jsonObject;
    }

    private JSONObject getDataObject() {
        JSONObject dataObject = new JSONObject();
        JSONObject markerObj = getMarkerObject();
        dataObject.put("x", this.xAxis.toArray());
        dataObject.put("y", this.yAxis.toArray());
        dataObject.put("text", texts.toArray());
        dataObject.put("mode", "markers");
        dataObject.put("marker", markerObj);
        return dataObject;
    }

    private JSONObject getMarkerObject() {
        JSONObject marker = new JSONObject();
        marker.put("color", this.colors.toArray());
        marker.put("size", this.size.toArray());
        return marker;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public List<Integer> getyAxis() {
        return yAxis;
    }

    public List<Double> getSize() {
        return size;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getTexts() { return texts; }
}
