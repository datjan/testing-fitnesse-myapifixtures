package myApiFixtures;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import fit.ColumnFixture;

public class MyApiSend extends ColumnFixture{
	

	public String SendRequest() throws IOException{
		
		String result = "failed";
		
		// Request Url
		CommunicationObject.url = new URL(CommunicationObject.GetUrl());
		CommunicationObject.connection = (HttpURLConnection) CommunicationObject.url.openConnection();
		// Request Method
		CommunicationObject.connection.setRequestMethod(CommunicationObject.req_method);
		// Request Header
	    for (int counter = 0; counter < CommunicationObject.req_header_name.size(); counter++) { 		      
	    	CommunicationObject.connection.setRequestProperty(CommunicationObject.req_header_name.get(counter), CommunicationObject.req_header_value.get(counter));
	    }  
		// Request Settings
		CommunicationObject.connection.setDoOutput(true);
		// Request Body
		if (!CommunicationObject.req_method.equals("GET")) {
			OutputStream os = CommunicationObject.connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
			osw.write(CommunicationObject.req_body);
			osw.flush();
			osw.close();
			os.close();  //don't forget to close the OutputStream
		}
		// Send
		CommunicationObject.connection.connect();
		// Response
		CommunicationObject.resp_message = CommunicationObject.connection.getResponseMessage();
		CommunicationObject.resp_code = CommunicationObject.connection.getResponseCode();
		// Response Body
		try {
			BufferedInputStream bis = new BufferedInputStream(CommunicationObject.connection.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result2 = bis.read();
			while(result2 != -1) {
			    buf.write((byte) result2);
			    result2 = bis.read();
			}
			CommunicationObject.resp_bodystring = buf.toString();
			
			result ="ok";
		} catch(Exception e) { 
	    	result = e.getMessage();
	    }  

		
		return result;
	}
	
	public String GetStatusMessage() throws IOException{
		return CommunicationObject.resp_message;
	}
	
	public int GetStatusCode() throws IOException{
		return CommunicationObject.resp_code;
	}
	
	public String GetUrl() throws IOException{
		return CommunicationObject.url.toString();
	}

}
