package controller;

import model.Frame;
import model.JavaClass;

import java.util.ArrayList;
import java.util.List;

public class FrameManager {
    private List<String> xAxis = new ArrayList<>();
    private List<Integer> yAxis = new ArrayList<>();
    private List<Double> size = new ArrayList<>();
    private List<String> colors = new ArrayList<>();
    private List<String> texts = new ArrayList<>();

    private List<Frame> frames = new ArrayList<>();

    private final double SIZE_SCALE_FACTOR = 1.5;
    private final double DEFAULT_SIZE = 10;

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

    public void saveFrame() {
        Frame frame = new Frame(xAxis, yAxis, size, colors, texts);
        this.frames.add(frame);
    }

    private String generateText() {
        return "";
    }

}
