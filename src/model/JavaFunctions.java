package model;

public class JavaFunctions {

    private String name;
    private int numOfOccurence;
    private int index;

    public String getName() {
        return name;
    }

    public int getNumOfOccurence() {
        return numOfOccurence;
    }

    public JavaFunctions(String name, int index) {
        this.name = name;
        this.numOfOccurence = 1;
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void incrementOccurence() {
        this.numOfOccurence += 1;
    }
}
