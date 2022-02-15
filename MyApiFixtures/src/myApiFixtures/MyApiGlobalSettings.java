package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiGlobalSettings extends ColumnFixture{

	String Parameter = "";
	String Value = "";

	public String Init() throws IOException{
		
		String result = "failed";
		
		try {
			
			if (Parameter.equals("base_url")) {
				CommunicationObject.req_baseurl = Value;
				result = "ok";
			}

			
	    } catch(Exception e) { 
	    	result = e.getMessage();
	    }  

		return result;
	}
	

	
}
