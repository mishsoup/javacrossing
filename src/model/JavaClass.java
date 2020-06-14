package model;

import java.util.HashMap;
import java.util.Map;

public class JavaClass {

    private String name;
    private String color;
    private Map<String, JavaFunctions> functions;
    private int currentYPoint = 0;

    private final int Y_SPACING = 5;

    public JavaClass(String name, String color) {
        this.name = name;
        this.color = color;
        functions = new HashMap<>();
    }

//    public void updateOrAddFunction(String fxnName, int index) {
//        if (this.functions.containsKey(fxnName)) {
//            updateFunction(fxnName);
//        } else {
//            addFunction(fxnName, index);
//        }
//    }

    public void updateFunction(String fxnName, long entryTime) {
        JavaFunctions jF = this.functions.get(fxnName);

        jF.increaseStartEndIndex();

        jF.incrementOccurence();
    }

    public boolean isFunctionMember(String fxnName) {
        return this.functions.containsKey(fxnName);
    }

    public void addFunction(String fxnName, int index, long entryTime) {
        JavaFunctions javaFunction = new JavaFunctions(fxnName, index, entryTime);
        currentYPoint += Y_SPACING;
        this.functions.put(fxnName, javaFunction);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public JavaFunctions getFunction(String functionName) {
        return this.functions.get(functionName);
    }

    public Map<String, JavaFunctions> getFunctions() {
        return functions;
    }

    public int getAvailableYPoint() {
        return currentYPoint;
    }
}
