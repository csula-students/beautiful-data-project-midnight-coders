package edu.csula.acquisition;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class IMDBCollector implements Collector<List<String>, List<String>> {
	MongoClient mongoClient;
	DB mongoDB;
	
	public IMDBCollector(){
		mongoClient = new MongoClient("localhost",
				27017);
		
		 mongoDB = mongoClient.getDB("tvshows");
		
	}
	public Collection<List<String>> mungee(Collection<List<String>> src) {
		return src;
	}

	public void save(String path, String mongoCollection) {
		DBCollection collection = mongoDB
					.getCollection(mongoCollection);
		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			String StringFromInputStream = IOUtils.toString(fis, "UTF-8");
			// System.out.println(StringFromInputStream);
			InputStream stream = new ByteArrayInputStream(StringFromInputStream.getBytes(StandardCharsets.UTF_8));

			for (Iterator it = new ObjectMapper().readValues(new JsonFactory().createJsonParser(stream), Map.class); it
					.hasNext();) {

				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> keyValue = (LinkedHashMap<String, String>) it.next();
				//System.out.println(keyValue.get("Rating"));
				BasicDBObject document = new BasicDBObject();
				   document.append("Title",keyValue.get("Name") );
				   document.append("Rating", keyValue.get("Rating"));
				   document.append("Plot", keyValue.get("Plot"));
				   document.append("nbVote",keyValue.get("Vote"));
				   document.append("Genre", keyValue.get("Genre"));
				   collection.insert(document);
			
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
