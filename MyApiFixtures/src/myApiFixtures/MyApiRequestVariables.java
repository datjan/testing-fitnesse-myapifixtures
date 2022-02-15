package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiRequestVariables extends ColumnFixture{
	
	String Name;
	String Value;

	public String Init() throws IOException{
		
		CommunicationObject.req_body = CommunicationObject.req_body.replace("#{" + Name + "}", Value).replace("${" + Name + "}", Value);
		
		return "ok";
	}
	
	public String Output() throws IOException{
		
		return Value;
	}
	
}
