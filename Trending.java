import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import twitter4j.*;

public class Trending
{
	static String[] getTrends() throws TwitterException, UnknownHostException, IOException, JSONException
	{
		//Instantiate and define variables and such here.
		TwitterFactory factory = new TwitterFactory();
		Twitter twitter = factory.getInstance();
		Trends trends = twitter.getPlaceTrends(1);
		ArrayList<String> trendTags = new ArrayList<String>();
		
		//Go through each trend in the JSON and add it's name to the trendTags ArrayList.
		for (Trend trend : trends.getTrends())
		{
			trendTags.add(trend.getName());
		}
		
		//Convert our trendTags ArrayList to an array.
		String[] trendingNames = trendTags.toArray(new String[trendTags.size()]);
		
		return trendingNames;
	}
}