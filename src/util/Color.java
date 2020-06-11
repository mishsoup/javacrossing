package util;

import java.util.*;

public class Color {

    private List<Colors> availableColors;

    public Color() {
        this.availableColors = new LinkedList<>();
        populateColors();
    }

    private void populateColors() {
        availableColors.addAll(Arrays.asList(Colors.values()));
    }

    public Colors getColor() {
        if (availableColors.size() < 1) {
            availableColors.addAll(Arrays.asList(Colors.values()));
            System.out.println("WARNING - Used all existing colors, reusing color set");
        }
        Colors retVal = availableColors.get(0);
        availableColors.remove(0);
        return retVal;
    }

}
