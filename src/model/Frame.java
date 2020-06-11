package model;

import org.json.JSONObject;
import util.Colors;

import java.util.List;

public class Frame {

    private List<String> xAxis;
    private List<Integer> yAxis;
    private List<Integer> size;
    private List<Colors> colors;

    public Frame(List<String> xAxis, List<Integer> yAxis, List<Integer> size, List<Colors> colors) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.size = size;
        this.colors = colors;
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

    public List<Integer> getSize() {
        return size;
    }

    public List<Colors> getColors() {
        return colors;
    }
}
