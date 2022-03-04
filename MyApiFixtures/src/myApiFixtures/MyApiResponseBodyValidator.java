package myApiFixtures;

import java.io.IOException;
import org.json.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fit.ColumnFixture;

public class MyApiResponseBodyValidator extends ColumnFixture{

	String Parser = "";
	String Identifier = "";
	String Variable = "";

	public String Value() throws IOException{
		
		String result = "failed";
		
		// Set Variable
		if (!Variable.equals("")) {
			Identifier = Identifier.replace("#{var}", Variable);
		}
		
		// Select Parser
		if (Parser.equals("json")) {
			result = Json_Parser(Identifier);
		} 
		else {
			result = "Parser must be defined. Already supported: json";
		}
		
		return result;
	}
	
	
	
	public String Json_Parser(String Identifier) throws IOException{
		String result = "";
		
		String[] identifier_array = Identifier.split("\\.");
		
		Integer identifier_lenght = helperStringArraySizeCounter(identifier_array);
        
		
		// Das erste Object muss den Namen root tragen (Bsp.: root oder root[2])
		if (identifier_array[0].contains("root")) 
		{
			// Wenn kein Array
			if (!identifier_array[0].contains("["))
			{
				JSONObject obj = new JSONObject(CommunicationObject.resp_bodystring);
				result = Json_Rekursive(obj,identifier_array,0);
			}
			// Wenn Array
			else
			{
				// Wenn Array und letzter Identifier
				if (identifier_array[0].contains("[") && 1==identifier_lenght) 
				{
					// Ermittle die eckigen Klammern des Identifier 
				    String identifier_insidebrackets = helperGetValueInSquaredBrackets(identifier_array[0].toString());
					
					// Ermittle Inhalt der eckigen Klammern
					identifier_insidebrackets = identifier_insidebrackets.replace("[", "").replace("]", "");
					
					// Prüfe ob das Objekt ein JSONArrray ist
					if (CommunicationObject.resp_bodystring.substring(0, 1).equals("[")) 
					{
						
						// Aktuelles Json Objekt als neues Json Array
						JSONArray temp_arr = new JSONArray(CommunicationObject.resp_bodystring);
						
						// Wenn Wert in eckigen Klammern eine Zahl ist (Bsp.: pointofsales[1])
						if (identifier_insidebrackets.chars().allMatch( Character::isDigit )) 
						{
	
							// Ausgabe Value der Position des Array
							result = String.valueOf(temp_arr.get(Integer.parseInt(identifier_insidebrackets)));
							
						}
						// Wenn Wert in eckigen Klammern ein Command ist
						else if (identifier_insidebrackets.equals("count"))
						{
							result = String.valueOf(temp_arr.length());
						}
						// Wenn Wert in eckigen Klammern ist sonstiges
						else
						{
							
							// Split Wert aus eckigen Klammern bei =
							String[] insidebrackets_array = identifier_insidebrackets.split("=");
							
							Integer insidebrackets_lenght = helperStringArraySizeCounter(insidebrackets_array);
					        
					        // Es müssen zwei Werte in den eckigen Klammer stehen
					        if (insidebrackets_lenght==2)
					        {
						        Boolean name_found = false;
								
						        // Iteriere durch alle Objekte und suche nach bracket inhalt match
								for (int i = 0; i < temp_arr.length(); i++)
								{
									
									Object array_object = temp_arr.get(i);
									// Wenn das Array JSONObjecte beinhaltet
									if (array_object instanceof JSONObject) 
									{
										// Ermittle Value Type und Value des aktuellen Objekt-Namen
										String actual_value = helperGetTypeOfJSONObjectValue(temp_arr.getJSONObject(i), insidebrackets_array[0].replace("\"", ""));
	
										// Ermittle Objekt-Wert der gesucht wird (entferne mögliche Anführungsstriche)
										String search_value = insidebrackets_array[1].replace("\"", "");
										
										
									    // Suche nach matching Objekt-Value
										if (actual_value.equals(search_value))
										{
											name_found = true;
											
											// Ausgabe Value der Position des Array
											result = String.valueOf(temp_arr.get(i));
	
										}
									}
									// Wenn das Array kein JSONObject beinhaltet
									else
									{
										name_found = true;
										result = "Syntax error: Within the brackets in last object you need an integer: root.beispiel[55]";
									}
				
								}
								
								// Wenn Objekt-Value nicht gefunden
								if (!name_found)
								{
									result = "Parser error: Not found: " + identifier_insidebrackets;
								}
								
	
					        }
					        else
					        {
					        	result = "Syntax error: Within the brackets you need a name:value pair: [name:value]";
					        }
					        					
						}
						
			        }
			        else
			        {
			        	result = "Parser error: No array found at this position: root";
			        }

				}
				// Wenn Array und nicht letzter Identifier
				else
				{
					
					// Ermittle die eckigen Klammern des Identifier 
				    String identifier_insidebrackets = helperGetValueInSquaredBrackets(identifier_array[0].toString());
					
					// Ermittle Inhalt der eckigen Klammern
					identifier_insidebrackets = identifier_insidebrackets.replace("[", "").replace("]", "");
					
					// Prüfe ob das Objekt ein JSONArrray ist
					if (CommunicationObject.resp_bodystring.substring(0, 1).equals("[")) 
					{
						
						// Aktuelles Json Objekt als neues Json Array
						JSONArray temp_arr = new JSONArray(CommunicationObject.resp_bodystring);
				
						// Wenn Wert in eckigen Klammern eine Zahl ist (Bsp.: pointofsales[1])
						if (identifier_insidebrackets.chars().allMatch( Character::isDigit )) 
						{
	
							// Aktuelles Json Array Position als neues Json Objekt
							JSONObject obj = temp_arr.getJSONObject(Integer.parseInt(identifier_insidebrackets));
								
							// Rekursiver Aufruf dieser Funktion für das aktuelle Json Objekt
							result = Json_Rekursive(obj, identifier_array, 0);
						}
						// Wenn Wert in eckigen Klammer keine Zahl ist (Bsp.: pointofsales[name="Ebay"])
						else 
						{
							// Split Wert aus eckigen Klammern bei =
							String[] insidebrackets_array = identifier_insidebrackets.split("=");
							
							Integer insidebrackets_lenght = helperStringArraySizeCounter(insidebrackets_array);
						        
					        // Es müssen zwei Werte in den eckigen Klammer stehen
					        if (insidebrackets_lenght==2)
					        {
						        Boolean name_found = false;
									
								// Iteriere durch alle Objekte und suche nach bracket inhalt match
								for (int i = 0; i < temp_arr.length(); i++)
								{
									// Ermittle Value Type und Value des aktuellen Objekt-Namen
									String actual_value = helperGetTypeOfJSONObjectValue(temp_arr.getJSONObject(i), insidebrackets_array[0].replace("\"", ""));
				
									// Ermittle Objekt-Wert der gesucht wird (entferne mögliche Anführungsstriche)
									String search_value = insidebrackets_array[1].replace("\"", "");
										
										
									// Suche nach matching Objekt-Value
									if (actual_value.equals(search_value))
									{
										name_found = true;
										
										// Aktuelles Json Array Position als neues Json Objekt
										JSONObject obj = temp_arr.getJSONObject(i);
										
										// Rekursiver Aufruf dieser Funktion für das aktuelle Json Objekt
										result = Json_Rekursive(obj, identifier_array, 0);
									}
					
								}
									
								// Wenn Objekt-Value nicht gefunden
								if (!name_found)
								{
									result = "Parser error: Not found: " + identifier_insidebrackets;
								}
					        }
					        else
					        {
						       	result = "Syntax error: Within the brackets you need a name:value pair: [name:value]";
						    }
	
						}
						
			        }
			        else
			        {
			        	result = "Parser error: No array found at this position: root";
			        }
					
				}
				
			}
			
			
		}
		else
		{
			result = "Syntax error: The first object needs to be root: root.example";
		}
		
	
		
		return result;
	}
	
	public String Json_Rekursive(JSONObject obj, String[] identifier_array, Integer level_counter) throws IOException{
		String result = "";
		
		// OBJ = JSON Objekt der aktuellen Suchposition
		// IDENTIFIER = Kette von Identifiern zum erkennen der Position im Json (Bsp.: root.responseinfo.code oder root.basicdata.pointofsales[1].name)
		// LEVEL_COUNTER (start 0) = Nach jedem Check des eines Identifiers wird der Level um eins erhöht (Bsp: root.responseinfo.code = 1.2.3)
		
		JSONArray temp_arr;
		
		Integer identifier_lenght = helperStringArraySizeCounter(identifier_array);

        
        // Zähle counter einen hoch
		level_counter++;
		Integer identifier_actual = level_counter + 1;
		

		
		// Wenn kein Array und letzter Identifier
		if (!identifier_array[level_counter].contains("[") && identifier_actual==identifier_lenght) 
		{		
			// Wenn es das Objekt-Name gibt
			if (obj.has(identifier_array[level_counter].toString()))
			{
				// Ausgabe des aktuellen Objekts passend zum Identifier
				result = String.valueOf(obj.get(identifier_array[level_counter].toString()));
			} else {
				result = "Parser error: Not found: " + identifier_array[level_counter].toString();
			}
		} 
		// Wenn kein Array und nicht letzter Identifier
		else if (!identifier_array[level_counter].contains("[") && identifier_actual<identifier_lenght) 
		{		
			// Wenn es das Objekt-Name gibt
			if (obj.has(identifier_array[level_counter].toString()))
			{
				// Aktuelles Json Objekt als neues Json Objekt
				obj = obj.getJSONObject(identifier_array[level_counter].toString());
				
				// Rekursiver Aufruf dieser Funktion für das aktuelle Json Objekt
				result = Json_Rekursive(obj, identifier_array, level_counter);
			} else {
				result = "Parser error: Not found: " + identifier_array[level_counter].toString();
			}
		} 
		// Wenn Array und letzter Identifier
		else if (identifier_array[level_counter].contains("[") && identifier_actual==identifier_lenght) 
		{
			// Ermittle die eckigen Klammern des Identifier 
		    String identifier_insidebrackets = helperGetValueInSquaredBrackets(identifier_array[level_counter].toString());
		    
			// Ermittle aktuellen Identifier ohne eckige Klammern
			String identifier_nobrackets = identifier_array[level_counter].toString().replace(identifier_insidebrackets, "");
			
			// Ermittle Inhalt der eckigen Klammern
			identifier_insidebrackets = identifier_insidebrackets.replace("[", "").replace("]", "");
			
			// Prüfe ob das Objekt ein JSONArrray ist
			Object temp_obj = obj.get(identifier_nobrackets);
			if (temp_obj instanceof JSONArray) 
			{
				
				// Aktuelles Json Objekt als neues Json Array
				temp_arr = obj.getJSONArray(identifier_nobrackets);
				
				// Wenn Wert in eckigen Klammern eine Zahl ist (Bsp.: pointofsales[1])
				if (identifier_insidebrackets.chars().allMatch( Character::isDigit )) 
				{
					// Ausgabe Value der Position des Array
					result = String.valueOf(temp_arr.get(Integer.parseInt(identifier_insidebrackets)));
				}
				// Wenn Wert in eckigen Klammern ein Command ist
				else if (identifier_insidebrackets.equals("count"))
				{
					result = String.valueOf(temp_arr.length());
				}
				// Wenn Wert in eckigen Klammern ist sonstiges
				else
				{
	
					// Split Wert aus eckigen Klammern bei =
					String[] insidebrackets_array = identifier_insidebrackets.split("=");
					
					Integer insidebrackets_lenght = helperStringArraySizeCounter(insidebrackets_array);
			        
			        // Es müssen zwei Werte in den eckigen Klammer stehen
			        if (insidebrackets_lenght==2)
			        {
				        Boolean name_found = false;
						
				        // Iteriere durch alle Objekte und suche nach bracket inhalt match
						for (int i = 0; i < temp_arr.length(); i++)
						{
							
							Object array_object = temp_arr.get(i);
							// Wenn das Array JSONObjecte beinhaltet
							if (array_object instanceof JSONObject) 
							{
								// Ermittle Value Type und Value des aktuellen Objekt-Namen
								String actual_value = helperGetTypeOfJSONObjectValue(temp_arr.getJSONObject(i), insidebrackets_array[0].replace("\"", ""));
	
								// Ermittle Objekt-Wert der gesucht wird (entferne mögliche Anführungsstriche)
								String search_value = insidebrackets_array[1].replace("\"", "");
								
								
							    // Suche nach matching Objekt-Value
								if (actual_value.equals(search_value))
								{
									name_found = true;
									
									// Ausgabe Value der Position des Array
									result = String.valueOf(temp_arr.get(i));
	
								}
							}
							// Wenn das Array kein JSONObject beinhaltet
							else
							{
								name_found = true;
								result = "Syntax error: Within the brackets in last object you need an integer: root.beispiel[55]";
							}
		
						}
						
						// Wenn Objekt-Value nicht gefunden
						if (!name_found)
						{
							result = "Parser error: Not found: " + identifier_insidebrackets;
						}
						
	
			        }
			        else
			        {
			        	result = "Syntax error: Within the brackets you need a name:value pair: [name:value]";
			        }	
					
				}
				
	        }
	        else
	        {
	        	result = "Parser error: No array found at this position: " + identifier_nobrackets;
	        }	
			
		}
		// Wenn Array und nicht letzter Identifier
		else if (identifier_array[level_counter].contains("[") && identifier_actual<identifier_lenght) 
		{
			// Ermittle die eckigen Klammern des Identifier 
		    String identifier_insidebrackets = helperGetValueInSquaredBrackets(identifier_array[level_counter].toString());
		    
			// Ermittle aktuellen Identifier ohne eckige Klammern
			String identifier_nobrackets = identifier_array[level_counter].toString().replace(identifier_insidebrackets, "");
			
			// Ermittle Inhalt der eckigen Klammern
			identifier_insidebrackets = identifier_insidebrackets.replace("[", "").replace("]", "");
			
			// Wenn es das Objekt-Name gibt
			if (obj.has(identifier_nobrackets))
			{
				// Prüfe ob das Objekt ein JSONArrray ist
				Object temp_obj = obj.get(identifier_nobrackets);
				if (temp_obj instanceof JSONArray) 
				{
					
					// Aktuelles Json Objekt als neues Json Array
					temp_arr = obj.getJSONArray(identifier_nobrackets);
		
					// Wenn Wert in eckigen Klammern eine Zahl ist (Bsp.: pointofsales[1])
					if (identifier_insidebrackets.chars().allMatch( Character::isDigit )) 
					{
	
						// Aktuelles Json Array Position als neues Json Objekt
						obj = temp_arr.getJSONObject(Integer.parseInt(identifier_insidebrackets));
							
						// Rekursiver Aufruf dieser Funktion für das aktuelle Json Objekt
						result = Json_Rekursive(obj, identifier_array, level_counter);
	
					}
					// Wenn Wert in eckigen Klammer keine Zahl ist (Bsp.: pointofsales[name="Ebay"])
					else {
						// Split Wert aus eckigen Klammern bei =
						String[] insidebrackets_array = identifier_insidebrackets.split("=");
						
						Integer insidebrackets_lenght = helperStringArraySizeCounter(insidebrackets_array);
				        
				        // Es müssen zwei Werte in den eckigen Klammer stehen
				        if (insidebrackets_lenght==2)
				        {
					        Boolean name_found = false;
							
							// Iteriere durch alle Objekte und suche nach bracket inhalt match
							for (int i = 0; i < temp_arr.length(); i++)
							{
								// Ermittle Value Type und Value des aktuellen Objekt-Namen
								String actual_value = helperGetTypeOfJSONObjectValue(temp_arr.getJSONObject(i), insidebrackets_array[0].replace("\"", ""));
	
								// Ermittle Objekt-Wert der gesucht wird (entferne mögliche Anführungsstriche)
								String search_value = insidebrackets_array[1].replace("\"", "");
								
								
							    // Suche nach matching Objekt-Value
								if (actual_value.equals(search_value))
								{
									name_found = true;
									
									// Aktuelles Json Array Position als neues Json Objekt
									obj = temp_arr.getJSONObject(i);
									
									// Rekursiver Aufruf dieser Funktion für das aktuelle Json Objekt
									result = Json_Rekursive(obj, identifier_array, level_counter);
	
								}
			
							}
							
							// Wenn Objekt-Value nicht gefunden
							if (!name_found)
							{
								result = "Parser error: Not found: " + identifier_insidebrackets;
							}
				        }
				        else
				        {
				        	result = "Syntax error: Within the brackets you need a name:value pair: [name:value]";
				        }
	
					}
					
		        }
		        else
		        {
		        	result = "Parser error: No array found at this position: " + identifier_nobrackets;
		        }
			} else {
				result = "Parser error: Not found: " + identifier_nobrackets;
			}		
		}
		else {
			result = "General error: failed on " + level_counter + " level - identifier_lenght: " + identifier_lenght;
		}
		
		return result;
	}
	
	
	
	
	
	
	public String helperGetValueInSquaredBrackets(String value) throws IOException{
		String result = "";
		
		// Ermittle die eckigen Klammern des Identifier 
	    final Pattern pattern = Pattern.compile( "\\[([^\\]]*)\\]" );
	    final Matcher matcher = pattern.matcher(value);

	    while ( matcher.find() ) {
	    	result = matcher.group(0) ;
	    }
	    
	    return result;
	}
	
	public Integer helperStringArraySizeCounter(String[] string_array) throws IOException{
		Integer result = 0;
		
        for(String i:string_array)
        {
        	result++;    //Increment the count variable
        }
        
        return result;
	}
	
	public String helperGetTypeOfJSONObjectValue(JSONObject jsonobj, String name) throws IOException{
		String result = "";
		
		Object actual_value_object = jsonobj.get(name);
		
		if (actual_value_object instanceof String) {
			// Ermittle Objekt-Value als String
			result = jsonobj.getString(name);
		}
		else if (actual_value_object instanceof Integer) {
			// Ermittle Objekt-Value als Integer
			result = Integer.toString(jsonobj.getInt(name));
		}
		else if (actual_value_object instanceof Long) {
			// Ermittle Objekt-Value als Long
			result = Long.toString(jsonobj.getLong(name));
		}
		else if (actual_value_object instanceof Float) {
			// Ermittle Objekt-Value als Float
			result = Float.toString(jsonobj.getFloat(name));
		}
		else if (actual_value_object instanceof Double) {
			// Ermittle Objekt-Value als Double
			result = Double.toString(jsonobj.getDouble(name));
		}
		else
		{
			// Ermittle Objekt-Value als String
			result = jsonobj.getString(name);
		}
		
		return result;
	}
	
	
}
