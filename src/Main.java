import parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser();
        String testString = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello world!\");\n" +
                "\n" +
                "    }\n" +
                "}\n";

        testParser.parseJavaFile(testString);

    }
}
