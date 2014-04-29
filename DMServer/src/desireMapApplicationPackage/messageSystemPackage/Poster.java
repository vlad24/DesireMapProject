package desireMapApplicationPackage.messageSystemPackage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.codehaus.jackson.map.ObjectMapper;


public class Poster {
	 public static void post(ServerMessage serverMessage){
		 try {
			 System.out.println("...posting");
			// 1. Define URL
			URL url = new URL(MessageConstants.GoogleSenderURL);
			// 2. Open connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			// 4. Set the properties of the connection
			connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Authorization", "key=" + MessageConstants.APIKey);
		    connection.setDoOutput(true);
		    // 5. Add JSON data into POST request body , using Jackson object mapper to convert preparedMessage object into JSON
		    ObjectMapper mapper = new ObjectMapper();
		    //mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		    DataOutputStream connectionOutputStream = new DataOutputStream(connection.getOutputStream());
		    mapper.writeValue(connectionOutputStream, serverMessage);
		    // 6. Sending
		    connectionOutputStream.flush();
		    // 7. close
		    connectionOutputStream.close();
            // 8. Get the response
            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            //9. ???
            BufferedReader inReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inReader.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println(response.toString());
            inReader.close();
            System.out.println("inReader has been closed");
			
		} catch (IOException e) {
			System.out.println("-Posting failed : " + e.getMessage());
			e.printStackTrace();
		}
	 }
}
