package parser;

import reader.StringFileContent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private String mainDirPath;
    private String importString;

    // regex taken from Stackoverflow post
    // https://stackoverflow.com/questions/37403641/regex-to-fetch-the-correct-java-class-name
    private final String regexForClass = "(?<=\\n|\\A)(?:public\\s)?(class|abstract class|enum)\\s([^\\n\\s]*)";
    // regex taken from Stackoverflow post
    // https://stackoverflow.com/questions/35912934/how-to-match-a-method-block-using-regex
    private final String regexForFunc= "((?:(?:public|private|protected|static|final|synchronized|volatile)\\s+)*)\\s*(\\w+)\\s*(\\w+)\\(.*?\\)\\s*";
    String className = "Class name not found";
    String funcName = "Function name not found";
    Boolean hasReturn = false;
    int countOfBracket = 0;
    Boolean stillInFunCall = false;

    public String getMainDirPath() {
        return mainDirPath;
    }

    public Map<String, StringFileContent> parse(Map<String, StringFileContent> contents) {
        // get mainFileDir
        mainDirPath = findMainFileDirPath(contents);
        // make importString
        //1) get dir name
        String dirName = mainDirPath.substring(mainDirPath.lastIndexOf("/") + 1);
        //2) make importString
        importString = "import "+ dirName+ ".OutputCreator; \n";
        // parse files
        Map<String, StringFileContent> newFileContents = new HashMap<>();
        contents.forEach((k, v) -> {
            String newV = parseJavaFile(v.getContent());
            StringFileContent newStringFileContent = new StringFileContent(newV);
            newFileContents.put(k, newStringFileContent);
        });
        return newFileContents;
    }

    private String findMainFileDirPath(Map<String, StringFileContent> fileContentMap) {
        StringBuffer mainPath = new StringBuffer();
        // get Main.java path
        fileContentMap.forEach((k, v) -> {
            if (isMainPath(k)) {
                mainPath.append(k);
            }
        });
        int lastSlashIndex = mainPath.lastIndexOf("/");
        // cut it before Main.java  (path to Main.java dir)
        return mainPath.substring(0, lastSlashIndex);
    }

    private boolean isMainPath(String mainPath) {
        int lastSlashIndex = mainPath.lastIndexOf("/");
        return mainPath.substring(lastSlashIndex).contains("Main");
    }

    private String parseJavaFile(String javaString) {
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
        for (int i = 0; i < javaStringAry.length; i++) {
            String currentLine = javaStringAry[i];
            String nextLine = null;
            if ((i+1) < javaStringAry.length)
            {
                nextLine = javaStringAry[i+1];
            }
            Pattern pattern = Pattern.compile(regexForClass);
            Matcher classM = pattern.matcher(currentLine);
            Pattern patternFunc = Pattern.compile(regexForFunc);
            Matcher funcM = patternFunc.matcher(currentLine);
            Pattern SuperPattern = Pattern.compile("super\\(.*\\)");
            Matcher funcMSuper = SuperPattern.matcher(currentLine);
            // regex taken from stack over flow post
            // https://stackoverflow.com/questions/18037179/check-if-the-line-contains
            Pattern patternComment = Pattern.compile("//.*|/\\*((.|\\n)(?!=*/))+\\*/");
            Matcher funcComment = patternComment.matcher(currentLine);
            if (funcMSuper.find()) {
                outputString.append(processSuper(currentLine) + "\n");
            } else if (funcComment.find()) {
                outputString.append(currentLine + "\n");
            } else if (classM.find()) {
                className = findClassName(currentLine);
                outputString.append(importString + "\n");
                outputString.append(currentLine + "\n");
            } else if (funcM.find() && !stillInFunCall &&
                    currentLine.contains("{") && !currentLine.contains("if")
                    && !currentLine.contains("while") && !currentLine.contains("for")) {
                Pattern SuperPattern2 = Pattern.compile("super\\(.*\\)");
                Matcher funcMSuper2 = SuperPattern2.matcher(currentLine);
                if (funcMSuper2.find()) {
                    funcName = findFunctionName(currentLine);
                    continue;
                }
                outputString.append(processFuncNameFound(currentLine) + "\n");
            } else if (stillInFunCall){
                outputString.append(processStillInFunc(currentLine) + "\n");
            } else {
                outputString.append(currentLine + "\n");
            }
        }
        return outputString.toString();
    }

    private String processSuper(String currentLine) {
        stillInFunCall = true;
        currentLine = currentLine + "\n" + makeStartJSONCode();
        currentLine = checkHasReturnAndAddEndJSON(currentLine);
        processBrackets(currentLine);
        currentLine = checkEndBracketAndAddEndJSON(currentLine);

        // end of function parsing
        if (countOfBracket == 0 ){
            resetFuncVars();
        }
        return currentLine;
    }

    private String processStillInFunc(String currentLine) {
        // if has return, use return form add End JSON; otherwiese do nothing return back currentline
        currentLine = checkHasReturnAndAddEndJSON(currentLine);
        processBrackets(currentLine);
        currentLine = checkEndBracketAndAddEndJSON(currentLine);
        // end of function parsing
        if (countOfBracket == 0 ){
            resetFuncVars();
        }
        return currentLine;
    }

    private String processFuncNameFound(String currentLine) {
        stillInFunCall = true;
        funcName = findFunctionName(currentLine);
        currentLine = addStartJSON(currentLine);
        currentLine = checkHasReturnAndAddEndJSON(currentLine);
        processBrackets(currentLine);
        currentLine = checkEndBracketAndAddEndJSON(currentLine);

        // end of function parsing
        if (countOfBracket == 0 ){
            resetFuncVars();
        }
        return currentLine;
    }

    private String addStartJSON(String currentLine) {
        String res;
        res = currentLine.replaceFirst("\\{", makeStartJSONCode());

        return res;
    }

    // check whether has [return], if yes, add return type END JSON; otherwise no change in currentLine
    private String checkHasReturnAndAddEndJSON(String currentLine) {
        String res;
        if (currentLine.contains("return")) {
            res = currentLine.replaceAll(
                    "return",
                    makeReturnEndJSONCode());
            hasReturn = true;
            return res;
        } else {
            return currentLine;
        }
    }

    // check whether has }, if yes, add bracket type END JSON; otherwise no change in currentLine
    private String checkEndBracketAndAddEndJSON(String currentLine) {
        String res;
        if (!hasReturn && countOfBracket == 0 && currentLine.contains("}")) {
            int pos = currentLine.lastIndexOf('}');
            res = currentLine.substring(0, pos) + makeBracketEndJSONCode() + "    } \n";
            return res;
        }
        return currentLine;
    }

    private void resetFuncVars() {
        stillInFunCall = false;
        hasReturn = false;
        funcName = "Function name not found";
    }

    private void processBrackets(String currentLine) {
        countOfBracket += (int) currentLine.chars().filter(ch -> ch == '{').count();
        countOfBracket -= (int) currentLine.chars().filter(ch -> ch == '}').count();
        if (currentLine.contains("\\{"))  {
            countOfBracket -= 1;
        } else if (currentLine.contains("\\}")) {
            countOfBracket +=1;
        }
    }

    private String findClassName(String javaString) {
        Pattern pattern = Pattern.compile(regexForClass);
        Matcher m = pattern.matcher(javaString);
        if (m.find()) {
            String className = m.group(0);
            int pos = className.lastIndexOf("class");
//            String[] splitStr = javaString.split("\\s+");
            className = className.substring(pos+5).trim();
            return className;
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

    // input className; check whether the class is Main, we will append diff code to Main like write file
    private Boolean isMainFile(String fileName) {
        return fileName.equals("Main");
    }

    private String makeStartJSONCode() {
        String res;
        if (isMainFile(className)) {
            // injecting code to where the first { is found
            res = "{ \n" +
                    "        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();\n" +
                    "        outputCreator.addMainStartJSON(\""+className+"\", \"Start_\" + \""+funcName+"\");\n";
        } else {
            // injecting code to where the first { is found
            res = "{ \n" +
                    "        OutputCreator outputCreator = OutputCreator.getTheOutputCreator();\n" +
                    "        outputCreator.addFuncJSON(\""+className+"\", \"Start_\" + \""+funcName+"\");\n";
        }
        return res;
    }

    private String makeBracketEndJSONCode() {
        String res;
        if (isMainFile(className)) {
            res = "    outputCreator.addMainEndJSON(\"" + className + "\", \"End_\" + \"" + funcName + "\");\n" +
                    "    outputCreator.writeJSONFile(\"result.txt\");\n";
        } else {
            res = "    outputCreator.addFuncJSON(\"" + className + "\", \"End_\" + \"" + funcName + "\");\n";
        }
        return res;
    }

    private String makeReturnEndJSONCode() {
        String res;
        if (isMainFile(className)) {
            res = "outputCreator.addMainEndJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n " +
                            "outputCreator.writeJSONFile(\"result.txt\");\n" +
                            "       return";
        } else {
            res = "outputCreator.addFuncJSON(\""+className+"\", \"End_\" + \""+funcName+"\");\n " +
                            "       return";
        }
        return res;
    }

}
