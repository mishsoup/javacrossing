package model;

public class JavaFunctions {

    private String name;
    private int numOfOccurence;
    private long entryTime;
    private long totalTime;
    private int index;

    private int startEndIndex;

    public String getName() {
        return name;
    }

    public long getTotalTime(){
        return totalTime;
    }

    public long increaseTotalTime(long newTimeSpent) {
        totalTime += newTimeSpent;
        return totalTime;
    }

    public void updateTotalTime(long newTotalTime) { totalTime = newTotalTime;}

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

        this.startEndIndex = 1;
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

    public int getStartEndIndex() {return startEndIndex;}
    public void increaseStartEndIndex() {startEndIndex += 1;}
    public void decreaseStartEndIndex() {startEndIndex -= 1;}
    public boolean isFunEnd() {return startEndIndex == 0;}
}
