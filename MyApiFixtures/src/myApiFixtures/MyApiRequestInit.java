package myApiFixtures;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import fit.ColumnFixture;

public class MyApiRequestInit extends ColumnFixture{
	
	String Method = "";
	String Baseurl = "";
	String Path = "";
	String Body = "";
	String BodyFilePath = "";

	public String Init() throws IOException{
		
		String result = "failed";
		Boolean success = true;
		
		try {
			// Set default values
			CommunicationObject.req_method = "";
			CommunicationObject.req_baseurl = "";
			CommunicationObject.req_path = "";
			CommunicationObject.req_parameter = "";
			CommunicationObject.req_header_name = new ArrayList<String>();
			CommunicationObject.req_header_value = new ArrayList<String>();
			CommunicationObject.req_body = "";
			CommunicationObject.resp_code = 0;
			CommunicationObject.resp_message = "";
			CommunicationObject.resp_bodystring = "";
			
			// Set new parameter
			CommunicationObject.req_method = Method;
			CommunicationObject.req_baseurl = Baseurl;
			
			if (!Path.equals(""))
			{
				CommunicationObject.req_path = Path;
			}
			if (!Body.equals(""))
			{
				CommunicationObject.req_body = Body;
			}
			
			if (!BodyFilePath.equals(""))
			{
				InputStream input = new FileInputStream(BodyFilePath);
	
			    String text = null;
			    try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {
			        text = scanner.useDelimiter("\\A").next();
			    }
			    
				CommunicationObject.req_body = text;
			}
			
			result = "ok";
			
		} catch(Exception e) { 
	    	result = e.getMessage();
	    	success = false;
	    }  

		if (!success) {
			return result;
		} else {
			return "ok";
		}
		
	}
	
}