import parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser testParser = new Parser();
        String testString = "package visitors;\n" +
                "\n" +
                "import ast.*;\n" +
                "import exceptions.WrongNameException;\n" +
                "import libs.MusicCreator;\n" +
                "import org.jfugue.pattern.Pattern;\n" +
                "import org.jfugue.pattern.PatternProducer;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class PlayerVisitor implements Visitor<Map<String, Integer>, String>{\n" +
                "    static protected MusicCreator musicCreator = MusicCreator.getMusicCreator();\n" +
                "    private int defaultBPM = 100;\n" +
                "    public static Map<String, Integer> environmentTable = new HashMap<>();\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(PLAY play, Map<String, Integer> envTable) {\n" +
                "        NAME name = play.getName();\n" +
                "        String nameValue = name.getName();\n" +
                "        Pattern music = musicCreator.getSound(nameValue, envTable);\n" +
                "        musicCreator.getPlayer().play(music);\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(BEAT beat, Map<String, Integer> envTable) {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(CHORD chord, Map<String, Integer> envTable) {\n" +
                "        String octaveString = getOctave(chord.getOctave());\n" +
                "        String note = chord.getTheNote().substring(0,1);\n" +
                "        String tone = \"maj\";\n" +
                "        if (chord.getTheNote().substring(1).equals(\"m\")){\n" +
                "            tone = \"min\";\n" +
                "        }\n" +
                "        return note+octaveString+tone+chord.getLengths();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(CHORDPROGRESSION chordprogression, Map<String, Integer> envTable) {\n" +
                "        StringBuilder musicString = new StringBuilder(\"\");\n" +
                "        for (BASEKEY eachKey: chordprogression.getNotes()) {\n" +
                "            musicString.append(\" \" + eachKey.accept(this, envTable) + \" \");\n" +
                "        }\n" +
                "        // TODO maybe we can take this line out and just directly return the string\n" +
                "        // TODO to be considered later\n" +
                "        Pattern musicPattern = new Pattern(musicString.toString());\n" +
                "        return musicPattern.toString();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(COUNTS counts, Map<String, Integer> envTable) {\n" +
                "        return counts.getCounts();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(COUNTVALUE countvalue, Map<String, Integer> envTable) {\n" +
                "        return countvalue.getCountValue();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(CREATE create, Map<String, Integer> envTable) {\n" +
                "        NAME name = create.getName();\n" +
                "        SOUND sound = create.getSound();\n" +
                "        String songName = name.getName();\n" +
                "        Pattern pattern = new Pattern(sound.accept(this, envTable));\n" +
                "        musicCreator.addMusicToSongs(songName, pattern, envTable);\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(INSTRUMENT instrument, Map<String, Integer> envTable) {\n" +
                "        return instrument.getInstrument();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(CONNECT connect, Map<String, Integer> envTable) {\n" +
                "        List<NAME> names = connect.getSubNames();\n" +
                "        int nameListSize = names.size();\n" +
                "        Pattern song = new Pattern();\n" +
                "        for (int i = 0; i < nameListSize ; i++) {\n" +
                "            song.add(musicCreator.getSound(names.get(i).getName(), envTable));\n" +
                "        }\n" +
                "        musicCreator.addMusicToSongs(connect.getNewName().getName(), song, envTable);\n" +
                "        //TODO remove this line, only for debugging\n" +
                "        //System.out.println(song);\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(LENGTH length, Map<String, Integer> envTable) {\n" +
                "        return length.getLength();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(MELODY melody, Map<String, Integer> envTable) {\n" +
                "        StringBuilder musicString = new StringBuilder(\"\");\n" +
                "        for (BASEKEY eachKey: melody.getNotes()) {\n" +
                "            musicString.append(\" \" + eachKey.accept(this, envTable) + \" \");\n" +
                "        }\n" +
                "        // TODO maybe we can take this line out and just directly return the string\n" +
                "        // TODO to be considered later\n" +
                "        Pattern musicPattern = new Pattern(musicString.toString());\n" +
                "        return musicPattern.toString();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(NAME name, Map<String, Integer> envTable) {\n" +
                "        return name.getName();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(NOTE note, Map<String, Integer> envTable) {\n" +
                "        String octaveString = getOctave(note.getOctave());\n" +
                "        return note.getTheNote()+octaveString+note.getLengths();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(PROGRAM program, Map<String, Integer> envTable) {\n" +
                "        for (FUNC eachFunc: program.getFuncs()) {\n" +
                "            eachFunc.accept(this, envTable);\n" +
                "        }\n" +
                "        for (INSTRUCTION eachInstruction: program.getInstructions()) {\n" +
                "            eachInstruction.accept(this, envTable);\n" +
                "        }\n" +
                "        if (program.getPlay() != null) {\n" +
                "            program.getPlay().accept(this, envTable);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(REST rest, Map<String, Integer> envTable) {\n" +
                "        return \"R\"+rest.getLengths();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(SOUND sound, Map<String, Integer> envTable) {\n" +
                "        BEAT beat = sound.getBeat();\n" +
                "        COUNTS counts = beat.getCounts();\n" +
                "        COUNTVALUE countvalue = beat.getCountvalue();\n" +
                "        Pattern musicPattern = new Pattern(\"TIME:\" + counts.getCounts() + \"/\" + countvalue.getCountValue());\n" +
                "        musicPattern.add(sound.getBaseSound().accept(this, envTable));\n" +
                "        INSTRUMENT instrument = sound.getInstrument();\n" +
                "        musicPattern.setInstrument(instrument.getInstrument());\n" +
                "        if (sound.getBpm() != null) {\n" +
                "            musicPattern.setTempo(Integer.parseInt(sound.getBpm().getBpm()));\n" +
                "        } else {\n" +
                "            musicPattern.setTempo(defaultBPM);\n" +
                "        }\n" +
                "        return musicPattern.toString();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(LAYER layer, Map<String, Integer> envTable) {\n" +
                "        List<NAME> names = layer.getSubNames();\n" +
                "        int nameListSize = names.size();\n" +
                "        Pattern song = new Pattern();\n" +
                "        for (int i = 0; i < nameListSize ; i++) {\n" +
                "            Pattern newSongSetName = new Pattern(musicCreator.getSound(names.get(i).getName(), envTable));\n" +
                "            newSongSetName.setVoice(i);\n" +
                "            song.add(newSongSetName);\n" +
                "        }\n" +
                "        musicCreator.addMusicToSongs(layer.getNewName().getName(), song, envTable);\n" +
                "        //TODO remove this line, only for debugging\n" +
                "        System.out.println(song);\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(BPM bpm, Map<String, Integer> envTable) {\n" +
                "        return bpm.getBpm();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(COMMENT comment, Map<String, Integer> envTable) {\n" +
                "        return comment.getComment();\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(RUN run, Map<String, Integer> envTable) {\n" +
                "        String functionName = run.getFuncName();\n" +
                "        List<String> paraValues = run.getParaNames();\n" +
                "        FUNCBODY funcbody = musicCreator.getFuncbody(functionName, envTable);\n" +
                "        List<String> paraNames = funcbody.getParaNames();\n" +
                "\n" +
                "        Map<String, Integer> funEnv = new HashMap<>();\n" +
                "\n" +
                "        for (int i = 0; i < paraNames.size(); i++) {\n" +
                "            String inputValueName = paraValues.get(i);\n" +
                "            String paraName = paraNames.get(i);\n" +
                "\n" +
                "            Object object = musicCreator.getObject(inputValueName, envTable);\n" +
                "            if (funcbody.getTableForCheckScope().get(paraName)) {\n" +
                "                Integer oldAddress = envTable.get(inputValueName);\n" +
                "                funEnv.put(paraName, oldAddress);\n" +
                "            }\n" +
                "\n" +
                "            musicCreator.addObjectToSongs(paraName, object, funEnv);\n" +
                "        }\n" +
                "        funcbody.accept(this, funEnv);\n" +
                "\n" +
                "        // we need to clean the memory which point by funEnv\n" +
                "        // 1) find out all not REF parameters' address\n" +
                "        Map<String, Boolean> funcScopeTable = funcbody.getTableForCheckScope();\n" +
                "        List<String> nonRefName = new ArrayList<>();\n" +
                "        for (Map.Entry<String,Boolean> entry : funcScopeTable.entrySet()) {\n" +
                "            if (!entry.getValue()) {\n" +
                "                nonRefName.add(entry.getKey());\n" +
                "            }\n" +
                "        }\n" +
                "        List<Integer> nonRefAddress = new ArrayList<>();\n" +
                "        for (String eachName: nonRefName) {\n" +
                "            nonRefAddress.add(funEnv.get(eachName));\n" +
                "        }\n" +
                "\n" +
                "        // 2) use this address delete it in memoryTable\n" +
                "        for (Integer eachAddress: nonRefAddress) {\n" +
                "            musicCreator.getMemoryTable().remove(eachAddress);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(FUNC func, Map<String, Integer> envTable) {\n" +
                "        String name = func.getName();\n" +
                "        FUNCBODY funcbody = func.getFuncbody();\n" +
                "        musicCreator.addFuncBodyToSongs(name, funcbody, envTable);\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String evaluate(FUNCBODY funcbody,  Map<String, Integer> envTable) {\n" +
                "        for (INSTRUCTION eachInstruction: funcbody.getInstructions()) {\n" +
                "            eachInstruction.accept(this, envTable);\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    private String getOctave(String octaveString){\n" +
                "        if (octaveString == null) {\n" +
                "            return \"5\";\n" +
                "        }\n" +
                "        String sign = octaveString.substring(0,1);\n" +
                "        int octave;\n" +
                "        switch(sign) {\n" +
                "            case \"+\":\n" +
                "                octave = 5 + Integer.valueOf(octaveString.substring(1));\n" +
                "                if (octave > 10) {\n" +
                "                    octave = 10;\n" +
                "                }\n" +
                "                break;\n" +
                "            case \"-\":\n" +
                "                octave = 5 - Integer.valueOf(octaveString.substring(1));\n" +
                "                if (octave < 0) {\n" +
                "                    octave = 0;\n" +
                "                }\n" +
                "                break;\n" +
                "            default:\n" +
                "                return \"5\";\n" +
                "        }\n" +
                "        return Integer.toString(octave);\n" +
                "    }\n" +
                "}\n";

        testParser.parseJavaFile(testString);

    }
}
