package edu.csula.acquisition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	
	public static JSONObject readJsonFromUrl(String url) throws IOException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String jsonText = readAll(rd);
	      JSONObject json = (JSONObject) JSONValue.parse(jsonText);;
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	@SuppressWarnings("unchecked")
	public static  void JsonWriteForIMDB(String title, String rating, String genre , String vote ,String plot) {
		JSONObject jsonObj = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		jsonObj.put("Name", title);
		jsonObj.put("Rating", rating);
		jsonObj.put("Genre", genre);
		jsonObj.put("Vote", vote);
		jsonObj.put("Plot", plot);
		jsonArray.add(jsonObj);
		
		File dir = new File("Data");
		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (SecurityException se) {

			}
		}

		File f = new File("Data/IMDB_Ratings.json");

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