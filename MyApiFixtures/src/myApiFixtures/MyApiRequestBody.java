package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiRequestBody extends ColumnFixture{
	
	String Body;
	String RequestFilePath;

	public String Init() throws IOException{
		
		CommunicationObject.req_body = Body;
		
		return "ok";
	}
	
	public String AddRow() throws IOException{
		
		CommunicationObject.req_body = CommunicationObject.req_body + Body + System.lineSeparator();
		
		return "ok";
	}
	
	public String GetBody() throws IOException{
		
		return CommunicationObject.req_body;
	}
	

	
}
