package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private final String importString = "import parser.OutputCreator; \n";
    // regex taken from Stackoverflow post
    // https://stackoverflow.com/questions/37403641/regex-to-fetch-the-correct-java-class-name
    private final String regexForClass = "(?<=\\n|\\A)(?:public\\s)?(class|abstract class|enum)\\s([^\\n\\s]*)";
    // regex taken from Stackoverflow post
    // https://stackoverflow.com/questions/35912934/how-to-match-a-method-block-using-regex
    private final String regexForFunc= "((?:(?:public|private|protected|static|final|synchronized|volatile)\\s+)*)\\s*(\\w+)\\s*(\\w+)\\(.*?\\)\\s*";

    public String parseJavaFile(String javaString) {
        // if the java passed in is an interface then no modification required
        if (checkIfInterface(javaString)) {
            return javaString;
        }
        javaString = javaString.replaceAll("import parser.OutputCreator; \n", "");
        // split into string to parse each line
        String javaStringAry[] = javaString.split("\\r?\\n");
        String outputString = processEachLine(javaStringAry);
        // TODO remove this only for testing
        System.out.println(outputString);
        return outputString;
    }

    private String processEachLine(String[] javaStringAry) {
        StringBuilder outputString = new StringBuilder("");
        String className = "Class name not found";
        String funcName = "Function name not found";
        Boolean hasReturn = false;
        int countOfBracket = 0;
        Boolean stillInFunCall = false;
        for (int i = 0; i < javaStringAry.length; i++) {
            String currentLine = javaStringAry[i];
            Pattern pattern = Pattern.compile(regexForClass);
            Matcher classM = pattern.matcher(currentLine);
            Pattern patternFunc = Pattern.compile(regexForFunc);
            Matcher funcM = patternFunc.matcher(currentLine);
            if (classM.find()) {
                className = findClassName(currentLine);
                outputString.append(importString + "\n");
                outputString.append(currentLine + "\n");
            } else if (funcM.find() && !stillInFunCall && currentLine.contains("{")) {
                stillInFunCall = true;
                funcName = findFunctionName(currentLine);
                    // injecting code to where the first { is found
                currentLine = currentLine.replaceFirst("\\{", "{ \n" +
                            "        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();\n" +
                            "        outputCreator.addJSON(\""+className+"\", \"Start_\" + \""+funcName+"\");\n");

                if (currentLine.contains("return")) {
                    currentLine = currentLine.replaceAll(
                            "return",
                            "outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n " +
                                    "       return");
                    hasReturn = true;
                }
                countOfBracket += (int) currentLine.chars().filter(ch -> ch == '{').count();
                countOfBracket -= (int) currentLine.chars().filter(ch -> ch == '}').count();
                if (!hasReturn && countOfBracket == 0 && currentLine.contains("}")) {
                    int pos = currentLine.lastIndexOf('}');
                    currentLine = currentLine.substring(0,pos) +
                            "    outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n"
                            + currentLine.substring(pos+1)
                            + "    } \n";
                }
                // end of function parsing
                if (countOfBracket == 0 ){
                    stillInFunCall = false;
                    hasReturn = false;
                    funcName = "Function name not found";
                }
                outputString.append(currentLine + "\n");
            } else if (stillInFunCall){
                if (currentLine.contains("return")) {
                    currentLine = currentLine.replaceAll(
                            "return",
                            "outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n " +
                                    "       return");
                    hasReturn = true;
                }
                countOfBracket += (int) currentLine.chars().filter(ch -> ch == '{').count();
                countOfBracket -= (int) currentLine.chars().filter(ch -> ch == '}').count();
                if (!hasReturn && countOfBracket == 0 && currentLine.contains("}")) {
                    int pos = currentLine.lastIndexOf('}');
                    currentLine = currentLine.substring(0,pos) +
                            "    outputCreator.addJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n"
                            + currentLine.substring(pos+1)
                            + "    } \n";
                }
                // end of function parsing
                if (countOfBracket == 0 ){
                    stillInFunCall = false;
                    hasReturn = false;
                    funcName = "Function name not found";
                }
                outputString.append(currentLine + "\n");
            } else {
                outputString.append(currentLine + "\n");
            }
        }
        return outputString.toString();
    }

    private String findClassName(String javaString) {
        Pattern pattern = Pattern.compile(regexForClass);
        Matcher m = pattern.matcher(javaString);
        if (m.find()) {
            String classnName = m.group(0);
            String[] splitStr = classnName.split("\\s+");
            return splitStr[splitStr.length - 1];
        } else {
            throw new RuntimeException("Cannot find class name.");
        }
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

    private Boolean checkIfInterface(String javaString) {
        // regex taken from Stackoverflow post
        // https://stackoverflow.com/questions/37403641/regex-to-fetch-the-correct-java-class-name
        Pattern pattern = Pattern.compile("(?<=\\n|\\A)(?:public\\s)?(interface)\\s([^\\n\\s]*)");
        Matcher m = pattern.matcher(javaString);
        return m.find();
    }
}
