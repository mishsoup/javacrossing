package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    // singleton outputcreator class which will extract when the function gets called
    // and create a json object
    OutputCreator outputCreator = OutputCreator.getTheOutputCreator();

    private final String importString = "import parser.OutputCreator; \n";
    private final String functionStartString = "";
    // this regex is taken from Stackoverflow post
    // https://stackoverflow.com/questions/68633/regex-that-will-match-a-java-method-declaration
    private String functionRegex = "(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?";

    public String parseJavaFile(String javaString) {
        // if the java passed in is an interface then no modification required
        if (checkIfInterface(javaString)) {
            return javaString;
        }
        String className = findClassName(javaString);
        // start building the new modified java file as a string
        StringBuilder outputString = new StringBuilder(importString);

        String[] arrOfJavawoFunc = javaString.split(functionRegex);
        outputString.append(arrOfJavawoFunc[0]);
        Pattern pattern = Pattern.compile(functionRegex);
        Matcher m = pattern.matcher(javaString);
        while (m.find()) {
            String funcName = findFunctionName(m.group(0));
            String injectedFunc = injectToFunc(m.group(0), className, funcName);
            System.out.println(injectedFunc);
            outputString.append(injectedFunc);
        }

        outputString.append(arrOfJavawoFunc[arrOfJavawoFunc.length - 1]);
        return outputString.toString();
    }

    private String injectToFunc(String functionString, String className, String funcName) {
        // replacing the first { found
        String replacedString = functionString.replaceFirst("\\{", "{ \n" +
                "        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();\n" +
                "        outputCreator.addString(\""+className+"\", \"Start_\" + \""+funcName+"\");");

        // if return is found in the function then inject before return
        String secondReplacedString;
        if (replacedString.matches("return")) {
            // TODO need to find ALL the returns and inject code before them
            secondReplacedString = "";
        } else {
            int pos = replacedString.lastIndexOf('}');
             secondReplacedString = replacedString.substring(0,pos) +
                    "    outputCreator.addString(\""+className+"\", \"End_\" + \""+funcName+"\");\n"
                    + replacedString.substring(pos+1)
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
            throw new RuntimeException("Cannot find function name");
        }
    }

    private String findClassName(String javaString) {
        Pattern pattern = Pattern.compile("(?<=\\n|\\A)(?:public\\s)?(class|interface|enum|abstract class)\\s([^\\n\\s]*)");
        Matcher m = pattern.matcher(javaString);
        if (m.find()) {
            String classnName = m.group(0);
            String[] splitStr = classnName.split("\\s+");
            return splitStr[splitStr.length - 1];
        } else {
            throw new RuntimeException("Cannot find class name");
        }
    }

    private Boolean checkIfInterface(String javaString) {
        Pattern pattern = Pattern.compile("(?<=\\n|\\A)(?:public\\s)?(interface)\\s([^\\n\\s]*)");
        Matcher m = pattern.matcher(javaString);
        if (m.find())
           return true;
        else
            return false;

    }

}
