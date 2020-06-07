package parser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO REMOVE THIS CLASS WHEN NEW PARSER IS FINISHED
public class OldParser {
    private final String importString = "import parser.OutputCreator; \n";
    // this regex is taken from Stackoverflow post
    // https://stackoverflow.com/questions/68633/regex-that-will-match-a-java-method-declaration
    private final String functionRegex = "(?:(?:public|private|protected|static|final|native|synchronized|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?";

    public String parseJavaFile(String javaString) {
        String lines[] = javaString.split("\\r?\\n");
        System.out.println(Arrays.toString(lines));

        // if the java passed in is an interface then no modification required
        if (checkIfInterface(javaString)) {
            return javaString;
        }
        String className = findClassName(javaString);
        StringBuilder outputString = new StringBuilder(importString);
        String[] arrOfJavaWOFunc = javaString.split(functionRegex);
        // append all the non function declarations
        for (int i = 0; i < arrOfJavaWOFunc.length - 1; i++) {
            outputString.append(arrOfJavaWOFunc[i]);
        }
        // process the function declarations
        Pattern pattern = Pattern.compile(functionRegex);
        Matcher m = pattern.matcher(javaString);
        while (m.find()) {
            String funcName = findFunctionName(m.group(0));
            String injectedFunc = injectToFunc(m.group(0), className, funcName);
            outputString.append("\n\n"  + injectedFunc);
        }

        outputString.append(arrOfJavaWOFunc[arrOfJavaWOFunc.length - 1]);
        System.out.println(outputString.toString());
        return outputString.toString();
    }

    private String injectToFunc(String functionString, String className, String funcName) {
        // injecting code to where the first { is found
        String firstReplacedString = functionString.replaceFirst("\\{", "{ \n" +
                "        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();\n" +
                "        outputCreator.addJSON(\""+className+"\", \"Start_\" + \""+funcName+"\");");

        // if return is found in the function then inject before all the returns
        // else just insert before the last } found
        String secondReplacedString;
        if (firstReplacedString.contains("return")) {
            secondReplacedString = firstReplacedString.replaceAll(
                    "return",
                    "outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n " +
                            "       return");
        } else {
            int pos = firstReplacedString.lastIndexOf('}');
            secondReplacedString = firstReplacedString.substring(0,pos) +
                    "    outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n"
                    + firstReplacedString.substring(pos+1)
                    + "}";
        }
        return secondReplacedString;
    }

    private String findFunctionName(String functionString) {
        // regex taken from Stackoverflow post
        // https://stackoverflow.com/questions/47663648/javascript-regex-getting-function-name-from-string
        Pattern pattern = Pattern.compile("([a-zA-Z_{1}][a-zA-Z0-9_]+)(?=\\()");
        Matcher m = pattern.matcher(functionString);
        if (m.find()) {
            return m.group(0);
        } else {
            throw new RuntimeException("Cannot find function name.");
        }
    }

    private String findClassName(String javaString) {
        // regex taken from Stackoverflow post
        // https://stackoverflow.com/questions/37403641/regex-to-fetch-the-correct-java-class-name
        Pattern pattern = Pattern.compile("(?<=\\n|\\A)(?:public\\s)?(class|enum|abstract class)\\s([^\\n\\s]*)");
        Matcher m = pattern.matcher(javaString);
        if (m.find()) {
            String classnName = m.group(0);
            String[] splitStr = classnName.split("\\s+");
            return splitStr[splitStr.length - 1];
        } else {
            throw new RuntimeException("Cannot find class name.");
        }
    }

    private Boolean checkIfInterface(String javaString) {
        // regex taken from Stackoverflow post
        // https://stackoverflow.com/questions/37403641/regex-to-fetch-the-correct-java-class-name
        Pattern pattern = Pattern.compile("(?<=\\n|\\A)(?:public\\s)?(interface)\\s([^\\n\\s]*)");
        Matcher m = pattern.matcher(javaString);
        return m.find();
    }

}
