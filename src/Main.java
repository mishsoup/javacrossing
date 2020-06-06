import parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser();
        String testString = "package ast;\n" +
                "\n" +
                "import libs.Node;\n" +
                "\n" +
                "// NOTE REST CHORD extends BASEKEY\n" +
                "public abstract class BASEKEY extends Node {\n" +
                "    private String theNote = null;\n" +
                "    private String lengths = null;\n" +
                "    private String octave = null;\n" +
                "    public String lengthPattern = \"[s|i|q|h|w]+\";\n" +
                "    public String octavePattern = \"[\\\\-|\\\\+][1-5]\";\n" +
                "\n" +
                "\n" +
                "    //getter\n" +
                "    public String getLengths() {\n" +
                "        return lengths;\n" +
                "    }\n" +
                "    public String getOctave() {\n" +
                "        return octave;\n" +
                "    }\n" +
                "    public String getTheNote() {\n" +
                "        return theNote;\n" +
                "    }\n" +
                "\n" +
                "    //setter\n" +
                "    public void setLengths(String lengths) { this.lengths = lengths; }\n" +
                "    public void setOctave(String octave) { this.octave = octave; }\n" +
                "    public void setTheNote(String theNote) { this.theNote = theNote; }\n" +
                "\n" +
                "    public int getIndex(String str, String key1, String key2) {\n" +
                "        int addSignIndex = str.indexOf(key1);\n" +
                "        int minusSignIndex = str.indexOf(key2);\n" +
                "        return Math.max(addSignIndex, minusSignIndex);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        testParser.parseJavaFile(testString);

    }
}
