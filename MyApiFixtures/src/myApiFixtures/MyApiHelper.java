package myApiFixtures;

import java.io.IOException;

import fit.ColumnFixture;

public class MyApiHelper extends ColumnFixture{

	String Input = "";

	public String Output() throws IOException{
		
		try {
			
			return Input;
			
		} catch(Exception e) { 
			return e.getMessage();
	    }  

		
	}
}
