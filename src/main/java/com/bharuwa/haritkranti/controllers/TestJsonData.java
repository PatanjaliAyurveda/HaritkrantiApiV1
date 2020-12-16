package com.bharuwa.haritkranti.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestJsonData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String data ="{\"Status\":\"OK\",\"Generated Codes\":[{\"Id\":\"1100016\",\"Code\":\"VE-0012\"}]}";
	//	System.out.println(data);
		JSONObject rootObject = new JSONObject(data);
	    JSONArray resultsArray = rootObject.getJSONArray("Generated Codes");
	    String code = resultsArray.getJSONObject(0).getString("Code");
	    String id = resultsArray.getJSONObject(0).getString("Id");
	    System.out.println(code);
	    System.out.println(id);
	}

}
