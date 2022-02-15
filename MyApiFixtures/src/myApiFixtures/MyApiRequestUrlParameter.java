package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiRequestUrlParameter extends ColumnFixture{
	
	String Name;
	String Value;

	public String Init() throws IOException{
		
		if (CommunicationObject.req_parameter.equals("")) 
		{
			CommunicationObject.req_parameter = "?" + Name + "=" + Value;
		}
		else
		{
			CommunicationObject.req_parameter = CommunicationObject.req_parameter + "&" + Name + "=" + Value;
		}

		return "ok";
	}
	
	public String Output() throws IOException{
		
		return Value;
	}
	
}
