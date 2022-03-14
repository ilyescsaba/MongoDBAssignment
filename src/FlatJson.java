import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FlatJson {

    Scanner scan;

    public static void main(String[] args) {

        FlatJson flatJson = new FlatJson();

    }

    public static void printJsonObject(JSONObject jsonObj) {
        jsonObj.keySet().forEach(keyStr ->
        {
            Object keyvalue = jsonObj.get(keyStr);

            System.out.println("key: " + keyStr + " value: " + keyvalue);

            if (keyvalue instanceof JSONObject) {
                printJsonObject((JSONObject) keyvalue);
            }
        });
    }


    /**
     * String newKey held throughout all recursions
     * ifLastObject -> reset newKey
     * at every recursion when !JSONObject just add with newKey + currentKey
     * */

    static JSONObject flatJsonObject = new JSONObject();
    static String newKey = null;
    static boolean inJSONObject = false;
    static boolean isLastIteration = false;


    public void flattenJson(JSONObject jsonObj) {


        jsonObj.keySet().forEach(keyStr ->
        {

            Object keyvalue = jsonObj.get(keyStr);

            if (keyvalue instanceof JSONObject){


                isLastIteration((JSONObject) keyvalue);

                if (!inJSONObject){
                    inJSONObject = true;
                } else if (isLastIteration){
                    inJSONObject = false;
                }

                if (newKey != null){

                    newKey = newKey + "." + keyStr.toString();
                } else {
                    newKey = keyStr.toString();
                }
                flattenJson((JSONObject) keyvalue);
            } else {
                // if last iteration, set inJSONObject false
                if (newKey == null){

                    flatJsonObject.put(keyStr.toString(),keyvalue);

                } else {
                    flatJsonObject.put(newKey + "." + keyStr.toString(), keyvalue);
                }
            }
        });


        if (newKey != null && newKey.contains(".")) {
            newKey = newKey.substring(0, newKey.lastIndexOf("."));
        } else {
            newKey = null;
        }
        isLastIteration = false;

    }

    public void isLastIteration(JSONObject jsonObject){


        jsonObject.keySet().forEach(key -> {
            Object keyvalue = jsonObject.get(key);

            if (keyvalue instanceof JSONObject){
                isLastIteration = true;
            }

        });

    }

    // parsing JSON file"
    Object obj;

    {
        try {

            scan = new Scanner(System.in);
            System.out.println("PLEASE ENTER ABSOLUTE PATH OF JSON FILE TO BE FLATTENED:");
            String JSONPath = scan.nextLine();
            obj = new JSONParser().parse(new FileReader(JSONPath));

            // print initial JSON file
            System.out.println("INITIAL FILE:");
            JSONObject jo = (JSONObject) obj;
            printJsonObject(jo);

            flattenJson(jo);

            System.out.println("PLEASE ENTER ABSOLUTE PATH FOR JSON FILE TO BE SAVED");
            String FlatJSONPath = scan.nextLine();
            FileWriter fileWriter = new FileWriter(FlatJSONPath);
            fileWriter.write(flatJsonObject.toJSONString());
            fileWriter.flush();

            // flatten and print JSON file
            System.out.println("FLATTENED VERSION OF FILE:");
            printJsonObject(flatJsonObject);

        } catch (IOException | ParseException e) {
            System.err.println("Wrong path or format specified");
            e.printStackTrace();
        }
    }

}
