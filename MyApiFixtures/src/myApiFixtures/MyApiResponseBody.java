package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiResponseBody extends ColumnFixture{

	
	String Body;

	public String GetBody() throws IOException{
		
		
		return CommunicationObject.resp_bodystring;
	}
	
	public String SetBody() throws IOException{
		

		CommunicationObject.resp_bodystring = Body;
		
		return "ok";
	}
	
	public String ShowBody() throws IOException{
		

		CommunicationObject.resp_bodystring = Body;
		
		return "ok";
	}
	
}
