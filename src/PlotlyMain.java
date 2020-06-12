import controller.PlotlyController;
import org.json.JSONArray;
import reader.JsonReader;
import writer.JsonWriter;

import java.util.Map;

public class PlotlyMain {

    public static void main(String[] args) {
        JsonReader jr = new JsonReader();
        Map<String, JSONArray> jsonMap =  jr.extract("result.txt");
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
