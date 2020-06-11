package model;

public class JavaFunctions {

    private String name;
    private int numOfOccurence;

    public String getName() {
        return name;
    }

    public int getNumOfOccurence() {
        return numOfOccurence;
    }

    public JavaFunctions(String name) {
        this.name = name;
        this.numOfOccurence = 1;
    }

    public void incrementOccurence() {
        this.numOfOccurence += 1;
    }
}
