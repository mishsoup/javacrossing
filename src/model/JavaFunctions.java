package model;

public class JavaFunctions {

    private String name;
    private int numOfOccurence;
    private long entryTime;
    private long totalTime;
    private int index;

    public String getName() {
        return name;
    }

    public long getTotalTime(){
        return totalTime;
    }

    public long updateTotalTime(long newTimeSpent) {
        totalTime += newTimeSpent;
        return totalTime;
    }

    public int getNumOfOccurence() {
        return numOfOccurence;
    }

    public long getEntryTime() { return entryTime; }

    public JavaFunctions(String name, int index, long entryTime) {
        this.name = name;
        this.numOfOccurence = 1;
        this.entryTime = entryTime;
        this.index = index;
        this.totalTime = 0;
    }

    public int getIndex() {
        return this.index;
    }

    public void setEntryTime(long entryTime){
        this.entryTime = entryTime;
    };

    public void incrementOccurence() {
        this.numOfOccurence += 1;
    }
}
