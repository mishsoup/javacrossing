import controller.PlotlyController;
import org.json.JSONArray;
import reader.JsonReader;
import writer.JsonWriter;

import java.io.File;
import java.util.Map;

public class PlotlyMain {

    public static void main(String[] args) {
        JsonReader jr = new JsonReader();
        // get the data path
        String inputDic = "input";
        File input = new File("input");
        String[] childPath = input.list();
        if (childPath[1] == null) throw new RuntimeException("Can not find the input Project path");
        String dataPath = inputDic + "/" +  childPath[1] + "/result.txt";
        // read the data
        Map<String, JSONArray> jsonMap =  jr.extract(dataPath);
        JSONArray parsedResults = jsonMap.get(JsonReader.RESULT_KEY);

        PlotlyController plotlyController = new PlotlyController(parsedResults);
        plotlyController.drawPlotly();

        JsonWriter writer = new JsonWriter();
        String fileName = "data.json";
        writer.createFile(fileName);
        plotlyController.savePlotlyFramesToFile(fileName);
        System.out.println("END FOR PLOTLY");
    }
}
