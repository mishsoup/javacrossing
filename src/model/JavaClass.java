package model;

import java.util.ArrayList;
import java.util.List;

public class JavaClass {

    private String name;
    private String color;
    private List<JavaFunctions> functions;
    private int currentYPoint = 0;

    private final int Y_SPACING = 5;

    public JavaClass(String name, String color) {
        this.name = name;
        functions = new ArrayList<>();
    }

    public void addFunction(JavaFunctions function) {
        currentYPoint += Y_SPACING;
        this.functions.add(function);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<JavaFunctions> getFunctions() {
        return functions;
    }

    public int getAvailableYPoint() {
        return currentYPoint;
    }
}
