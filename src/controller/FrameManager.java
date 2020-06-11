package controller;

import model.JavaClass;

import java.util.ArrayList;
import java.util.List;

public class FrameManager {
    private List<String> xAxis;
    private List<Integer> yAxis;
    private List<Double> size;
    private List<String> colors;
    private List<String> texts;

    private final double SIZE_SCALE_FACTOR = 1.5;
    private final double DEFAULT_SIZE = 10;

    public FrameManager() {
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
        size = new ArrayList<>();
        colors = new ArrayList<>();
        texts = new ArrayList<>();
    }

    public void addDataPoint(JavaClass jClass, String text) {
        xAxis.add(jClass.getName());
        yAxis.add(jClass.getAvailableYPoint());
        size.add(DEFAULT_SIZE);
        colors.add(jClass.getColor());
        texts.add(text);
    }

    public void scaleDataPoint(int index) {
        double scaledSize = this.size.get(index) * SIZE_SCALE_FACTOR;
        this.size.set(index, scaledSize);
    }

}
