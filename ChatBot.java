import java.io.*;
import java.net.*;

public class ChatBot {

    public static void main(String[] args) throws Exception 
    {
        //Define our server, nickname, login, and channel variables.
        String server = "irc.freenode.net";
        String nick = "NoLiBot";
        String login = "simple_bot";
        String channel = "##NoLiChatBot";
        
        //Directly connect to the FreeNode IRC server by defining socket, writer, and reader.
        Socket socket = new Socket(server, 6667);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        //Log in to the FreeNode server and then flush writer.
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");
        writer.flush();
        
        //Read lines from the server until it tells us we have connected.
        String line = null;
        
        while ((line = reader.readLine( )) != null) 
        {
            if (line.indexOf("004") >= 0) 
            {
                //We've logged in therefore break out of the while loop.
                break;
            }
            else if (line.indexOf("433") >= 0) 
            {
            	//End the program because someone else is using the ChatBot's nickname.
                System.out.println("Nickname is already in use.");
                return;
            }
        }
        
        //Join the channel we listed in the channel variable and then flush writer.
        writer.write("JOIN " + channel + "\r\n");
        writer.flush( );
        
        //Keep reading lines from the server.
        while ((line = reader.readLine( )) != null) 
        {
            if (line.toLowerCase( ).startsWith("PING ")) 
            {
                //Respond to PINGs to prevent disconnecting, then flush the server.
                writer.write("PONG " + line.substring(5) + "\r\n");
                writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                writer.flush( );
            }
            
            if (line.toLowerCase().contains("weather") || line.toLowerCase().contains("forecast"))
            {
                //Print the raw line received by the bot.
                //System.out.println(line);
            	
            	//Define variables exclusive to this if statement here.
                String lineArray[] = line.split(" ");
            	String forecast = " ";
                
            	//Search the lineArray for something that fits the criteria for the usual American zipcode 
            	//and get the forecast for that area.
            	for (int i = 0; i <= lineArray.length - 1; i++)
            	{
            		if (lineArray[i].matches("^\\d{5}(?:[-\\s]\\d{4})?$"))
            		{
            			forecast = Weather.startWebRequest(lineArray[i]);
            			
            			//Post in IRC chat the forecast for all eligible zipcodes they listed, then flush writer.
            			writer.write("PRIVMSG " + channel + " :" + forecast + "\r\n");
            			writer.flush();
            		}
            	}
            	
            	//In the event of there being no eligible zipcodes, inform the user of possible causes.
            	if (forecast.equals(" "))
            	{
            		writer.write("PRIVMSG " + channel + " :" + "One of four things happened:\r\n");
            		writer.flush();
            		writer.write("PRIVMSG " + channel + " :" + "Your zipcode is used outside the U.S.\r\n");
            		writer.flush();
            		writer.write("PRIVMSG " + channel + " :" + "Your zipcode is one of a select few that don't follow the standard that all other U.S. zipcodes follow.\r\n");
            		writer.flush();
            		writer.write("PRIVMSG " + channel + " :" + "You forgot to put a space between the zipcode and any other words or characters.\r\n");
            		writer.flush();
            		writer.write("PRIVMSG " + channel + " :" + "Or you made the zipcode up on the spot and it wasn't a zipcode in use.\r\n");
            		writer.flush();
            	}
            }
            
            //If the user says anything with the word trending in it or anything similar, get the twitter trends.
            if (line.toLowerCase().contains("trending") || line.toLowerCase().contains("trend") || line.toLowerCase().contains("trends"))
            {
            	//Define variables that are exclusive to this if statement here.
            	String[] trends = Trending.getTrends();
            	String trending = "";
            	
            	//After getting the twitter trends and storing them in an array, Put them inside the trending String
            	//and separate them with a bar character to help with visibility. 
            	for (int i = 0; i <= trends.length - 1; i++)
            	{
            		trending = trending + " | " + trends[i];
            	}
            	
            	//Show the user the contents of the trending variable and then flush writer.
            	writer.write("PRIVMSG " + channel + " :" + trending + "\r\n");
        		writer.flush();
            }
        }
    }
}
