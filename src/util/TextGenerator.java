package util;

import model.JavaClass;
import model.JavaFunctions;

public class TextGenerator {

    public static String generateText(JavaClass jc, JavaFunctions jf) {
        return "CLASS: " + jc.getName() + "<br>" +
                "FUNCTION: " + jf.getName() + "<br>" +
                "NUMBER OF CALLS: " + jf.getNumOfOccurence();
    }
}
