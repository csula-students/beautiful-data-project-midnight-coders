package edu.csula.acquisition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonWriter {
	
	@SuppressWarnings("unchecked")
	public  void JsonWrite(String path, String name, String season) {
		JSONObject jsonObj = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		jsonObj.put("Name", name);
		jsonObj.put("URL", "http://www.tvsubtitles.net/"+path);
		jsonObj.put("Season", season);
		jsonArray.add(jsonObj);
		
		File dir = new File("Data");
		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (SecurityException se) {

			}
		}

		File f = new File("Data/Tvshows.json");

		BufferedWriter file = null;
		try {
			file = new BufferedWriter(new FileWriter(f, true));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			file.write(mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(jsonObj));
			// System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));

			file.newLine();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				file.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
