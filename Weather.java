import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather
{
    static String startWebRequest(String zip) throws IOException
    {
    	//Define variables and such here.
    	String weatherURL = "http://api.openweathermap.org/data/2.5/weather?zip=" + zip + "&APPID=26aa1d90a24c98fad4beaac70ddbf274";
    	StringBuilder result = new StringBuilder();
    	URL url = new URL(weatherURL);
    	
    	//Define the connection variable and then set it's request method.
    	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    	connection.setRequestMethod("GET");
    	
    	//Define our reader with what we find from the connection variable.
    	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	
    	//Create the line variable and then append what line picks up from the reader variable to the result variable.
    	String line;
    	
    	while ((line = reader.readLine()) != null) 
    	{
    		result.append(line);
    	}
    	
    	//Close the reader variable now that we're finished.
    	reader.close();
    	
    	//Print out our newly formed result string.
    	//System.out.println(result.toString());
        
    	//Store the retrieved forecast from parseJson to the forecast variable.
    	String forecast = parseJson(result.toString());
         
    	//Create a response using the known forecast and store it in the response variable.
    	String response = "The forecast for the zipcode " + zip + " will be " + forecast + ".";
         
    	return response;
    }
   
    static String parseJson(String json)
    {
    	//Define objects exclusive to this method here.
        JsonElement jsonElement = new JsonParser().parse(json);
        JsonObject  mainWeatherObject = jsonElement.getAsJsonObject();
        JsonArray weatherArray = mainWeatherObject.getAsJsonArray("weather");
        String forecast = null;
        
        //Convert contents of the JsonArray to a JsonObject,
        //then store in a string the relevant weather forecast information that we want. 
        for (int i = 0; i <= weatherArray.size() - 1; i++)
        {
        	JsonObject object = weatherArray.get(i).getAsJsonObject();
        	forecast = object.get("main").getAsString();
        }

        return forecast;  
    }
       
}

