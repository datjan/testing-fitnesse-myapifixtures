package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiRequestHeader extends ColumnFixture{
	
	String Name;
	String Value;

	public String Init() throws IOException{
		
		CommunicationObject.req_header_name.add(Name);
		CommunicationObject.req_header_value.add(Value);
		
		return "ok";
	}
	
	public String Output() throws IOException{
		
		return Value;
	}
	
}