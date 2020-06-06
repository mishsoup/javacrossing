import parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser();
        String testString = "import parser.Parser;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        Parser testParser = new Parser();\n" +
                "\n" +
                "        testParser.parseJavaFile(testString);\n" +
                "\n" +
                "    }\n" +
                "}";

        testParser.parseJavaFile(testString);

    }
}
