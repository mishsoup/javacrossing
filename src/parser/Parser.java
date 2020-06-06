package parser;

public class Parser {

    OutputCreator outputCreator = OutputCreator.getTheOutputCreator();
    private final String importString = "import parser.OutputCreator;";

    public void parseJavaFile(String java) {
        String output = importString + java;
        // TODO replace the java with the correct code inserted
        java.replaceAll("regex", "replacement");
        

    }
}
