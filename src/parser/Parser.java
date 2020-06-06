package parser;

public class Parser {

    OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
    private final String importString = "import parser.OutputCreator; \n";

    public String parseJavaFile(String java) {
        StringBuilder outputString = new StringBuilder(importString);


        System.out.println(outputString.toString());
        return outputString.toString();
    }

}
