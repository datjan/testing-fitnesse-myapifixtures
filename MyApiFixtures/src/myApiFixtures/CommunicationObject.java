package myApiFixtures;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommunicationObject {
	// Request URL
    public static String req_baseurl;
    public static String req_method;
    public static String req_path;
    public static String req_parameter;
    public static ArrayList<String> req_header_name = new ArrayList<String>();
    public static ArrayList<String> req_header_value = new ArrayList<String>();
    // Request BODY
    public static String req_body = "";
    // Sends
    public static URL url;
    public static HttpURLConnection connection;
    // Responses
    public static int resp_code;
    public static String resp_message;
    public static String resp_bodystring;

	public static String GetUrl() throws IOException{
		return CommunicationObject.req_baseurl + CommunicationObject.req_path + CommunicationObject.req_parameter;
	}
	
}
