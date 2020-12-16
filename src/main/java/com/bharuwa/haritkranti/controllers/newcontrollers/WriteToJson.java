package com.bharuwa.haritkranti.controllers.newcontrollers;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
public class WriteToJson {
	public static void main(String args[]) {
	      
	      JSONObject jsonObject = new JSONObject();
	      jsonObject.put("ID", "1");
	      jsonObject.put("First_Name", "Krishna Kasyap");
	      jsonObject.put("Last_Name", "Bhagavatula");
	      jsonObject.put("Date_Of_Birth", "1989-09-26");
	      jsonObject.put("Place_Of_Birth", "Vishakhapatnam");
	      jsonObject.put("Country", "25000");
	      //Creating a json array
	      JSONArray array = new JSONArray();
	      
	      array.put(jsonObject);
	      try {
	         FileWriter file = new FileWriter("D:/json_array_output.json");
	         file.write(array.toString());
	         file.close();
	      } catch (IOException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      System.out.println("JSON file created: "+jsonObject);
	   }
}
