package model;

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


    public JSONObject toJson() {
        return null;
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
