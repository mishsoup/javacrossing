import controller.PlotlyController;
import org.json.JSONArray;
import reader.JsonReader;
import writer.JsonWriter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PlotlyMain {

    public static void main(String[] args) {
        JsonReader jr = new JsonReader();
        // get the data path
        String inputDic = "input";
        File input = new File("input");
        String[] childPath = input.list();
        if (childPath == null) throw new RuntimeException("Can not find the input project path");
        List<String> childPathList = Arrays.asList(childPath);


        //TODO .DS_Store which is special file in Mac, we try to omit it
        StringBuffer subPathOfInput = new StringBuffer();
        childPathList.forEach( x -> {
            if (!x.contains("DS_Store")) {
                subPathOfInput.append(x);
            }
        });

        if ( subPathOfInput.length() == 0) throw new RuntimeException("Can not find the input Project path");
        String dataPath = inputDic + "/" +  subPathOfInput + "/result.txt";

        // read the data
        Map<String, JSONArray> jsonMap =  jr.extract(dataPath);
        // TODO HERE MAYBE cause problem KEVIN (not sure)
        // Map<String, JSONArray> jsonMap =  jr.extract("result.txt");
        JSONArray parsedResults = jsonMap.get(JsonReader.RESULT_KEY);

        PlotlyController plotlyController = new PlotlyController(parsedResults, 1);
        plotlyController.drawPlotly();

        JsonWriter writer = new JsonWriter();
        String fileName = "data.json";
        //String fileNameTime = "data_time.json";
        writer.createFile(fileName);
        //writer.createFile(fileNameTime);
        plotlyController.savePlotlyFramesToFile(fileName);
        System.out.println("END FOR PLOTLY");
    }
}